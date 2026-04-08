package fit.cvut.cz.api.repository

import fit.cvut.cz.api.models.Movie
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MovieRepository : JpaRepository<Movie, Long> {

    fun findByTitle(title: String): Optional<Movie>
    fun existsByTitle(title: String): Boolean
    fun existsByTitleAndIdNot(title: String, id: Long): Boolean
}