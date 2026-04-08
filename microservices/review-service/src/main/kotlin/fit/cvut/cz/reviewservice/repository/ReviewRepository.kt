package fit.cvut.cz.reviewservice.repository

import fit.cvut.cz.reviewservice.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {
    fun findAllByMovieId(movieId: Long): List<Review>
    fun findAllByUserId(userId: Long): List<Review>
}
