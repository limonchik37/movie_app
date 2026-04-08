package fit.cvut.cz.reviewservice.service

import fit.cvut.cz.reviewservice.dto.CreateReviewRequest
import fit.cvut.cz.reviewservice.dto.ReviewDTO
import fit.cvut.cz.reviewservice.kafka.ReviewCreatedEvent
import fit.cvut.cz.reviewservice.kafka.ReviewDeletedEvent
import fit.cvut.cz.reviewservice.kafka.ReviewEventProducer
import fit.cvut.cz.reviewservice.model.Review
import fit.cvut.cz.reviewservice.repository.ReviewRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val reviewEventProducer: ReviewEventProducer
) {
    fun getById(id: Long): ReviewDTO = reviewRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Review $id not found") }
        .toDto()

    fun getByMovie(movieId: Long): List<ReviewDTO> =
        reviewRepository.findAllByMovieId(movieId).map { it.toDto() }

    fun getByUser(userId: Long): List<ReviewDTO> =
        reviewRepository.findAllByUserId(userId).map { it.toDto() }

    @Transactional
    fun create(movieId: Long, userId: Long, req: CreateReviewRequest): ReviewDTO {
        val review = Review(
            userId = userId,
            movieId = movieId,
            text = req.text,
            photoPath = req.photoPath
        )
        val saved = reviewRepository.save(review)

        // Publish Kafka event asynchronously — notification-service will handle downstream logic
        reviewEventProducer.publishReviewCreated(
            ReviewCreatedEvent(
                reviewId = saved.id!!,
                movieId = saved.movieId,
                userId = saved.userId,
                text = saved.text
            )
        )

        return saved.toDto()
    }

    @Transactional
    fun update(reviewId: Long, userId: Long, req: CreateReviewRequest): ReviewDTO {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Review $reviewId not found") }
        if (review.userId != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify someone else's review")
        }
        review.text = req.text
        req.photoPath?.let { review.photoPath = it }
        return reviewRepository.save(review).toDto()
    }

    @Transactional
    fun delete(reviewId: Long, userId: Long) {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Review $reviewId not found") }
        if (review.userId != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete someone else's review")
        }
        reviewRepository.delete(review)

        reviewEventProducer.publishReviewDeleted(
            ReviewDeletedEvent(reviewId = reviewId, movieId = review.movieId, userId = userId)
        )
    }

    private fun Review.toDto() = ReviewDTO(
        id = id,
        userId = userId,
        movieId = movieId,
        text = text,
        photoPath = photoPath,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
