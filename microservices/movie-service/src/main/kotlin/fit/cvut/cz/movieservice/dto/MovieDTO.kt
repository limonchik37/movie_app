package fit.cvut.cz.movieservice.dto

import java.util.Date

data class MovieDTO(
    val id: Long? = null,
    val title: String,
    val year: Int,
    val genre: List<String>,
    val photoPath: String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

data class CreateMovieRequest(
    val title: String,
    val year: Int,
    val genre: List<String>,
    val photoPath: String? = null
)
