package cz.cvut.bekalim.app.data.review

import java.util.Date

data class ReviewDTO(
    val id: Long?,
    val userId: Long,
    val movieId: Long,
    val text: String,
    val photoPath: String?,
    val createdAt: Date?
)