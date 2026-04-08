package cz.cvut.bekalim.app.data.security

data class LoginRequest(
    val login: String,
    val password: String
)