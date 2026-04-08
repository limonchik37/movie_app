package fit.cvut.cz.api.service.impl

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.HeadBucketRequest
import software.amazon.awssdk.services.s3.model.NoSuchBucketException
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.net.URI
import java.time.Duration
import java.util.*

@Service
class B2ImageService(
    @Value("\${b2.endpoint}") private val endpointUrl: String,
    @Value("\${b2.bucket}") private val bucketName: String,
    @Value("\${b2.key-id}") private val keyId: String,
    @Value("\${b2.app-key}") private val appKey: String,
    @Value("\${b2.region:us-west-002}") private val region: String,
    @Value("\${b2.url-validity-minutes:10080}") private val urlValidityMinutes: Long
) {
    private lateinit var s3: S3Client
    private lateinit var presigner: S3Presigner

    @PostConstruct
    fun init() {

        val endpoint = URI.create(endpointUrl)
        s3 = S3Client.builder()
            .endpointOverride(endpoint)
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, appKey)))
            .build()

        presigner = S3Presigner.builder()
            .endpointOverride(endpoint)
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, appKey)))
            .build()

        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucketName).build())
            println("B2 bucket '$bucketName' exists")
        } catch (ex: S3Exception) {
            when (ex.statusCode()) {
                404 -> {
                    println("B2 bucket '$bucketName' not found, creating…")
                    s3.createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
                    println("B2 bucket '$bucketName' created.")
                }

                403 -> {
                    println("Access to B2 bucket '$bucketName' forbidden (403), assuming exists.")
                }

                else -> {
                    println("Error accessing bucket: HTTP ${ex.statusCode()}")
                }
            }
        }
    }

    fun upload(file: MultipartFile): String {
        val key = "${UUID.randomUUID()}-${file.originalFilename ?: "file"}"
        println("Uploading to B2 (private): $key")

        val putReq = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        s3.putObject(putReq, RequestBody.fromBytes(file.bytes))

        val getReq = software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(urlValidityMinutes))
            .getObjectRequest(getReq)
            .build()

        val presigned = presigner.presignGetObject(presignRequest)
        return presigned.url().toString()
    }
}