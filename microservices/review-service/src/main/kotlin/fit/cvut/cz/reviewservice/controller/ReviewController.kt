package fit.cvut.cz.reviewservice.controller

import fit.cvut.cz.reviewservice.dto.CreateReviewRequest
import fit.cvut.cz.reviewservice.dto.ReviewDTO
import fit.cvut.cz.reviewservice.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class ReviewController(private val reviewService: ReviewService) {

    // ── Read ──────────────────────────────────────────────────────────────────

    @GetMapping("/movies/{movieId}/reviews")
    fun getByMovie(@PathVariable movieId: Long): List<ReviewDTO> =
        reviewService.getByMovie(movieId)

    @GetMapping("/reviews/{id}")
    fun getById(@PathVariable id: Long): ReviewDTO =
        reviewService.getById(id)

    @GetMapping("/reviews/movie/{movieId}")
    fun getByMovieAlt(@PathVariable movieId: Long): List<ReviewDTO> =
        reviewService.getByMovie(movieId)

    // ── Write (JWT required) ──────────────────────────────────────────────────

    @PostMapping("/movies/{movieId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable movieId: Long,
        @RequestBody req: CreateReviewRequest
    ): ReviewDTO {
        val userId = extractUserId()
        return reviewService.create(movieId, userId, req)
    }

    @PutMapping("/movies/{movieId}/reviews/{reviewId}")
    fun update(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @RequestBody req: CreateReviewRequest
    ): ReviewDTO {
        val userId = extractUserId()
        return reviewService.update(reviewId, userId, req)
    }

    @DeleteMapping("/movies/{movieId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long
    ) {
        val userId = extractUserId()
        reviewService.delete(reviewId, userId)
    }

    // credentials field of UsernamePasswordAuthenticationToken carries userId (Long)
    private fun extractUserId(): Long {
        val auth = SecurityContextHolder.getContext().authentication
            as? UsernamePasswordAuthenticationToken
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated")
        return auth.credentials as Long
    }
}
