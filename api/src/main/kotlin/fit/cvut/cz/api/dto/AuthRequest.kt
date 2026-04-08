package fit.cvut.cz.api.dto

data class AuthRequest(
    var login: String? = null,
    val password: String? = null,
)