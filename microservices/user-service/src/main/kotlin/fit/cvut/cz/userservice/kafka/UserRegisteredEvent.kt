package fit.cvut.cz.userservice.kafka

import java.util.Date

data class UserRegisteredEvent(
    val userId: Long,
    val username: String,
    val registeredAt: Date = Date()
)
