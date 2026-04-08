package cz.cvut.bekalim.app.data.movie

import retrofit2.http.GET
import retrofit2.http.Path
import java.util.Date

interface MovieApi {
    @GET("movies")
    suspend fun getAll(): List<MovieDTO>

    @GET("movies/{id}")
    suspend fun getOne(@Path("id") id: Long): MovieDTO
}