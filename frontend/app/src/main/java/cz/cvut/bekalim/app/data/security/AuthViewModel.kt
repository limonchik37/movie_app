package cz.cvut.bekalim.app.data.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cz.cvut.bekalim.app.data.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    // ======== ЛОГИН ========
    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onLoginChanged(new: String) {
        _login.value = new
    }

    fun onPasswordChanged(new: String) {
        _password.value = new
    }

    /**
     * Выполняет логин, сохраняет token и userId в StateFlow
     */
    fun submitLogin(onResult: (Boolean) -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        _loginError.value = null

        repo.login(_login.value, _password.value)
            .onSuccess { resp ->
                NetworkModule.setJwt(resp.token)

                // 2) в свой стейт
                _token.value  = resp.token
                _userId.value = resp.userId

                onResult(true)
            }
            .onFailure { ex ->
                _loginError.value = ex.message ?: "Login failed"
                onResult(false)
            }

        _isLoading.value = false
    }

    // ======== РЕГИСТРАЦИЯ ========
    private val _regUsername = MutableStateFlow("")
    val regUsername: StateFlow<String> = _regUsername.asStateFlow()

    private val _regPassword = MutableStateFlow("")
    val regPassword: StateFlow<String> = _regPassword.asStateFlow()

    private val _regError = MutableStateFlow<String?>(null)
    val regError: StateFlow<String?> = _regError.asStateFlow()

    fun onRegUsernameChanged(new: String) {
        _regUsername.value = new
    }

    fun onRegPasswordChanged(new: String) {
        _regPassword.value = new
    }

    /**
     * Выполняет регистрацию, сохраняет userId
     */
    fun register(onResult: (Boolean) -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        _regError.value = null

        repo.register(_regUsername.value, _regPassword.value)
            .onSuccess { resp ->
                _userId.value = resp.userId
                onResult(true)
            }
            .onFailure { ex ->
                _regError.value = ex.message ?: "Registration failed"
                onResult(false)
            }

        _isLoading.value = false
    }

    // ======== Factory для ViewModelProvider ========
    class Factory(private val repo: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}