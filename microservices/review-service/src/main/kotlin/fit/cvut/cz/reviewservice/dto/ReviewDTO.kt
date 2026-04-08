package fit.cvut.cz.reviewservice.dto

import java.util.Date

data class ReviewDTO(
    val id: Long? = null,
    val userId: Long,
    val movieId: Long,
    val text: String,
    val photoPath: String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

data class CreateReviewRequest(
    val text: String,
    val photoPath: String? = null
)
