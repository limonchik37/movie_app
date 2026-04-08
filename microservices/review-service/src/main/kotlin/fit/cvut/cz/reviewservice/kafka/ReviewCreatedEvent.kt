package fit.cvut.cz.reviewservice.kafka

import java.util.Date

data class ReviewCreatedEvent(
    val reviewId: Long,
    val movieId: Long,
    val userId: Long,
    val text: String,
    val createdAt: Date = Date()
)

data class ReviewDeletedEvent(
    val reviewId: Long,
    val movieId: Long,
    val userId: Long
)
