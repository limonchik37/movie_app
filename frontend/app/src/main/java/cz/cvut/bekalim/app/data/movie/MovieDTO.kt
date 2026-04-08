package cz.cvut.bekalim.app.data.movie

data class MovieDTO(
    val id: Long?,
    val title: String,
    val year: Int,
    val genre: List<String>,
    val photoPath: String?,
    val userIds: List<Long>
)