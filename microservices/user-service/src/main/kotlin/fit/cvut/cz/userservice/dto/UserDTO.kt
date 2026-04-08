package fit.cvut.cz.userservice.dto

import fit.cvut.cz.userservice.model.Role
import java.util.Date

data class UserDTO(
    val id: Long? = null,
    val username: String,
    val password: String? = null,
    val profilePhoto: String? = null,
    val role: Role? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

data class AuthRequest(
    val login: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String
)

data class UpdateUserRequest(
    val username: String? = null,
    val profilePhoto: String? = null
)
