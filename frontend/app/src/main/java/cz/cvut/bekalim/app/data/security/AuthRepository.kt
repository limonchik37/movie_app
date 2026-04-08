package cz.cvut.bekalim.app.data.security


class AuthRepository(private val api: AuthApi) {
    suspend fun login(login: String, password: String): Result<LoginResponse> =
        runCatching { api.login(LoginRequest(login, password)) }

    suspend fun register(login: String, password: String): Result<LoginResponse> =
        runCatching { api.register(LoginRequest(login, password)) }
}