package fit.cvut.cz.notificationservice.kafka

import java.util.Date

// Shared event shapes — must match what producers send

data class ReviewCreatedEvent(
    val reviewId: Long = 0,
    val movieId: Long = 0,
    val userId: Long = 0,
    val text: String = "",
    val createdAt: Date = Date()
)

data class ReviewDeletedEvent(
    val reviewId: Long = 0,
    val movieId: Long = 0,
    val userId: Long = 0
)

data class UserRegisteredEvent(
    val userId: Long = 0,
    val username: String = "",
    val registeredAt: Date = Date()
)
