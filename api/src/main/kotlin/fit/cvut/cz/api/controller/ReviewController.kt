package fit.cvut.cz.api.controller

import fit.cvut.cz.api.dto.ReviewDTO
import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.exception.ValidationException
import fit.cvut.cz.api.exception.exceptionCodes.ReviewExceptionCodes
import fit.cvut.cz.api.mapper.ReviewMapper
import fit.cvut.cz.api.security.JwtAuthentication
import fit.cvut.cz.api.service.ReviewService
import fit.cvut.cz.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class ReviewController(
    private val reviewService: ReviewService,
    private val reviewMapper: ReviewMapper,
    private val userService: UserService
) {

    // === 1) Список отзывов по фильму ===
    @GetMapping("/movies/{movieId}/reviews")
    fun listByMovie(@PathVariable movieId: Long): List<ReviewDTO> {
        return reviewService.getByMovie(movieId)
            .map { reviewMapper.toDto(it) }
    }

    @GetMapping("/reviews/movie/{movieId}")
    fun getByMovie(@PathVariable movieId: Long): ResponseEntity<List<ReviewDTO>> {
        val list = reviewService.getByMovie(movieId)
            .map { reviewMapper.toDto(it) }
        return ResponseEntity.ok(list)
    }

    @GetMapping("/reviews/{id}")
    fun getById(@PathVariable id: Long): ReviewDTO {
        val entity = reviewService.getById(id)
        return reviewMapper.toDto(entity)
    }

    // === 3) Создать отзыв под конкретным фильмом ===
    @PostMapping("/movies/{movieId}/reviews")
    fun create(
        @PathVariable movieId: Long,
        @RequestBody dto: ReviewDTO
    ): ResponseEntity<ReviewDTO> {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthentication
        dto.userId = auth.userId!!
        dto.movieId = movieId

        val created = reviewService.create(dto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reviewMapper.toDto(created))
    }

    @PutMapping("/movies/{movieId}/reviews/{reviewId}")
    fun update(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @RequestBody dto: ReviewDTO
    ): ReviewDTO {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthentication
        if (auth.userId != dto.userId) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN, "You cannot modify someone else's review"
            )
        }
        val updated = reviewService.update(reviewId, auth.userId!!, dto)
        return reviewMapper.toDto(updated)
    }

    @DeleteMapping("/movies/{movieId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long
    ) {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthentication
        reviewService.delete(reviewId, auth.userId!!)
    }

    // === 6) Загрузка картинки к отзыву ===
    @PostMapping("/movies/{movieId}/reviews/{reviewId}/image")
    fun uploadImage(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @ModelAttribute dto: UploadDTO
    ): ReviewDTO {
        // здесь SecurityContextHolder аналогично
        dto.id = reviewId
        val updated = reviewService.upload(dto)
        return reviewMapper.toDto(updated)
    }
}