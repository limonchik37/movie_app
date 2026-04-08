package cz.cvut.bekalim.app.data.review

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddReviewViewModel(
    application: Application,
    private val repo: ReviewRepository,
    private val movieId: Long,
    private val userId: Long
) : AndroidViewModel(application) {

    var text     by mutableStateOf("")
    var photoUri by mutableStateOf<Uri?>(null)
    var isLoading by mutableStateOf(false)
    var error     by mutableStateOf<String?>(null)

    /**
     * 1) Create the review (w/o photo)
     * 2) If user picked a photo, upload it
     */
    fun submit(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (text.isBlank()) {
                error = "Text cannot be empty"
                onResult(false)
                return@launch
            }

            isLoading = true
            error     = null

            // 1) create
            val created = runCatching {
                repo.create(userId, movieId, text)
            }.getOrElse {
                error = it.message
                isLoading = false
                onResult(false)
                return@launch
            }

            // 2) upload image if any
            photoUri?.let { uri ->
                runCatching {
                    repo.uploadImage(
                        reviewId = created.id!!,
                        uri = uri,
                        context = getApplication<Application>().applicationContext
                    )
                }.onFailure {
                    error = it.message
                }
            }

            isLoading = false
            onResult(error == null)
        }
    }

    /** AndroidViewModelFactory so we can pass Application + our args */
    class Factory(
        private val app: Application,
        private val repo: ReviewRepository,
        private val movieId: Long,
        private val userId: Long
    ) : ViewModelProvider.AndroidViewModelFactory(app) {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddReviewViewModel::class.java)) {
                @Suppress("ReplaceCastWithInstanceOfCheck")
                return AddReviewViewModel(app, repo, movieId, userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}