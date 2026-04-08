package fit.cvut.cz.api.dto

import java.util.*

data class RegisterResponse(
    val id: Long,
    val username: String,
    val profilePhoto: String?,
    val movieIds: List<Long>,
    val role: String?,
    val createdAt: Date,
    val updatedAt: Date
)