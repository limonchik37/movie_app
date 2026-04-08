package fit.cvut.cz.api.repository

import fit.cvut.cz.api.models.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findAllByMovieId(movieId: Long): List<Review>
}