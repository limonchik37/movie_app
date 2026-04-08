package cz.cvut.bekalim.app.data.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MoviesUiState(
    val isLoading: Boolean = false,
    val movies: List<MovieDTO> = emptyList(),
    val error: String? = null
)

class MoviesViewModel(private val repo: MovieRepository) : ViewModel() {
    private val _ui = MutableStateFlow(MoviesUiState())
    val ui: StateFlow<MoviesUiState> = _ui.asStateFlow()

    init { load() }
    private fun load() = viewModelScope.launch {
        _ui.value = _ui.value.copy(isLoading = true, error = null)
        repo.fetchAll()
            .onSuccess { list -> _ui.value = MoviesUiState(movies = list) }
            .onFailure { ex -> _ui.value = MoviesUiState(error = ex.message) }
    }

    class Factory(private val repo: MovieRepository)
        : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T {
            if (c.isAssignableFrom(MoviesViewModel::class.java))
                return MoviesViewModel(repo) as T
            throw IllegalArgumentException("Unknown VM class ${c.name}")
        }
    }
}