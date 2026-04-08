package fit.cvut.cz.movieservice.service

import fit.cvut.cz.movieservice.dto.CreateMovieRequest
import fit.cvut.cz.movieservice.dto.MovieDTO
import fit.cvut.cz.movieservice.model.Movie
import fit.cvut.cz.movieservice.repository.MovieRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class MovieService(private val movieRepository: MovieRepository) {

    fun getAll(): List<MovieDTO> = movieRepository.findAll().map { it.toDto() }

    fun getById(id: Long): MovieDTO = movieRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Movie $id not found") }
        .toDto()

    @Transactional
    fun create(req: CreateMovieRequest): MovieDTO {
        if (movieRepository.existsByTitle(req.title)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Movie '${req.title}' already exists")
        }
        val movie = Movie(
            title = req.title,
            year = req.year,
            genre = req.genre.joinToString(","),
            photoPath = req.photoPath
        )
        return movieRepository.save(movie).toDto()
    }

    @Transactional
    fun update(id: Long, req: CreateMovieRequest): MovieDTO {
        val movie = movieRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Movie $id not found") }
        movie.title = req.title
        movie.year = req.year
        movie.genre = req.genre.joinToString(",")
        req.photoPath?.let { movie.photoPath = it }
        return movieRepository.save(movie).toDto()
    }

    @Transactional
    fun delete(id: Long) {
        if (!movieRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Movie $id not found")
        }
        movieRepository.deleteById(id)
    }

    private fun Movie.toDto() = MovieDTO(
        id = id,
        title = title,
        year = year,
        genre = genre.split(",").filter { it.isNotBlank() },
        photoPath = photoPath,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
