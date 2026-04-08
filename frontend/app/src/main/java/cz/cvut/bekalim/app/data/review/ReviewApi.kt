package cz.cvut.bekalim.app.data.review

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ReviewApi {
    @GET("/reviews/movie/{movieId}")
    suspend fun getByMovie(@Path("movieId") movieId: Long): List<ReviewDTO>

    @POST("/reviews")
    suspend fun create(@Body body: ReviewCreateRequest): ReviewDTO

    @Multipart
    @POST("/reviews/upload")
    suspend fun uploadImage(
        @Part("reviewId") reviewId: Long,
        @Part file: MultipartBody.Part
    )
}