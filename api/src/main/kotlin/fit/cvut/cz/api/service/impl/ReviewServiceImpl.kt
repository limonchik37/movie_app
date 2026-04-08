package fit.cvut.cz.api.service.impl

import fit.cvut.cz.api.dto.ReviewDTO
import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.exception.EntityNotFoundException
import fit.cvut.cz.api.exception.ValidationException
import fit.cvut.cz.api.exception.exceptionCodes.ReviewExceptionCodes
import fit.cvut.cz.api.mapper.ReviewMapper
import fit.cvut.cz.api.models.Review
import fit.cvut.cz.api.repository.ReviewRepository
import fit.cvut.cz.api.service.MovieService
import fit.cvut.cz.api.service.ReviewService
import fit.cvut.cz.api.service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val reviewMapper: ReviewMapper,
    @Lazy private val userService: UserService,
    @Lazy private val movieService: MovieService,
    private val b2ImageService: B2ImageService
) : ReviewService {

    @Transactional(readOnly = true)
    override fun getById(id: Long): Review =
        reviewRepository.findById(id).orElseThrow {
            EntityNotFoundException(ReviewExceptionCodes.REVIEW_NOT_FOUND, id)
        }

    @Transactional(readOnly = true)
    override fun getByMovie(movieId: Long): List<Review> =
        reviewRepository.findAllByMovieId(movieId)

    @Transactional
    override fun create(dto: ReviewDTO): Review {
        val entity = reviewMapper.toEntity(dto)
        entity.user = userService.getById(dto.userId)
        entity.movie = movieService.getById(dto.movieId)
        entity.createdAt = Date()
        return reviewRepository.save(entity)
    }

    @Transactional
    override fun update(id: Long, userId: Long, dto: ReviewDTO): Review {
        val existing = getById(id)
        if (existing.user?.id != userId) {
            throw ValidationException(ReviewExceptionCodes.REVIEW_FORBIDDEN, id)
        }
        existing.text = dto.text
        existing.photoPath = dto.photoPath
        existing.updatedAt = Date()
        return reviewRepository.save(existing)
    }

    @Transactional
    override fun delete(id: Long, userId: Long) {
        val existing = getById(id)
        if (existing.user?.id != userId) {
            throw ValidationException(ReviewExceptionCodes.REVIEW_FORBIDDEN, id)
        }
        reviewRepository.delete(existing)
    }

    @Transactional
    override fun upload(dto: UploadDTO): Review {
        val existing = getById(dto.id)
        existing.photoPath = b2ImageService.upload(dto.file)
        return reviewRepository.save(existing)
    }
}