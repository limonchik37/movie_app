package fit.cvut.cz.api.service

import fit.cvut.cz.api.dto.ReviewDTO
import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.models.Review

interface ReviewService {
    fun getById(id: Long): Review
    fun getByMovie(movieId: Long): List<Review>             // ← новый метод
    fun create(dto: ReviewDTO): Review
    fun update(id: Long, userId: Long, dto: ReviewDTO): Review
    fun delete(id: Long, userId: Long)
    fun upload(dto: UploadDTO): Review
}