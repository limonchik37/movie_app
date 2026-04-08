package cz.cvut.bekalim.app.data.security

data class LoginResponse(
    val token: String,
    val userId: Long

)