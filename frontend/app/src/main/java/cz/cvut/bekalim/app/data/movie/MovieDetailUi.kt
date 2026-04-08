package cz.cvut.bekalim.app.data.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cz.cvut.bekalim.app.data.review.ReviewDTO
import cz.cvut.bekalim.app.data.review.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MovieDetailUi(
    val isLoading: Boolean = false,
    val movie: MovieDTO? = null,
    val reviews: List<ReviewDTO> = emptyList(),
    val error: String? = null
)

class MovieDetailViewModel(
    private val movieRepo: MovieRepository,
    private val reviewRepo: ReviewRepository,
    private val movieId: Long
) : ViewModel() {
    private val _ui = MutableStateFlow(MovieDetailUi())
    val ui: StateFlow<MovieDetailUi> = _ui.asStateFlow()

    init { loadAll() }
    private fun loadAll() = viewModelScope.launch {
        _ui.value = _ui.value.copy(isLoading = true, error = null)
        val m = movieRepo.fetchOne(movieId)
        if (m.isFailure) {
            _ui.value = MovieDetailUi(error = m.exceptionOrNull()?.message)
            return@launch
        }
        val r = reviewRepo.fetchByMovie(movieId)
        _ui.value = if (r.isSuccess) {
            MovieDetailUi(movie = m.getOrNull(), reviews = r.getOrNull()!!)
        } else {
            MovieDetailUi(movie = m.getOrNull(), error = r.exceptionOrNull()?.message)
        }
    }

    class Factory(
        private val movieRepo: MovieRepository,
        private val reviewRepo: ReviewRepository,
        private val movieId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T {
            if (c.isAssignableFrom(MovieDetailViewModel::class.java))
                return MovieDetailViewModel(movieRepo, reviewRepo, movieId) as T
            throw IllegalArgumentException("Unknown VM class: ${c.name}")
        }
    }
}