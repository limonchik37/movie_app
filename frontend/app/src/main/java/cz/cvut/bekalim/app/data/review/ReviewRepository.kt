package cz.cvut.bekalim.app.data.review

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class ReviewRepository(private val api: ReviewApi) {

    /** Fetch all reviews for a movie */
    suspend fun fetchByMovie(movieId: Long) = runCatching {
        api.getByMovie(movieId)
    }

    /** Create a new review (we need userId, movieId, text) */
    suspend fun create(userId: Long, movieId: Long, text: String): ReviewDTO =
        api.create(
            ReviewCreateRequest(
                userId  = userId,
                movieId = movieId,
                text    = text
            )
        )

    /**
     * Upload an image to an existing review.
     * Turns your URI into a MultipartBody.Part.
     */
    suspend fun uploadImage(
        reviewId: Long,
        uri: Uri,
        context: Context
    ) = runCatching {
        // 1) open a stream and read bytes
        val stream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open $uri")
        val bytes = stream.readBytes()
        stream.close()

        // 2) figure out the MIME type (e.g. "image/jpeg")
        val mime = context.contentResolver.getType(uri)
            ?: "application/octet-stream"

        // 3) wrap bytes into a RequestBody
        val requestBody = bytes.toRequestBody(mime.toMediaTypeOrNull())

        // 4) create the form part; "file" must match your @Part name
        val part = MultipartBody.Part.createFormData(
            name     = "file",
            filename = "upload.jpg",    // or extract from uri.lastPathSegment
            body     = requestBody
        )

        // 5) call your Retrofit endpoint
        api.uploadImage(reviewId = reviewId, file = part)
    }
}