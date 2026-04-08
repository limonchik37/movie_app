package fit.cvut.cz.api.service.impl

import fit.cvut.cz.api.dto.MovieDTO
import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.exception.EntityNotFoundException
import fit.cvut.cz.api.exception.ValidationException
import fit.cvut.cz.api.exception.exceptionCodes.MovieExceptionCodes
import fit.cvut.cz.api.mapper.MovieMapper
import fit.cvut.cz.api.models.Movie
import fit.cvut.cz.api.repository.MovieRepository
import fit.cvut.cz.api.service.MovieService
import fit.cvut.cz.api.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class MovieServiceImpl(
    private val movieMapper: MovieMapper,
    private val movieRepository: MovieRepository,
    private val userService: UserService,
    private val b2ImageService: B2ImageService
) : MovieService {

    override fun getById(id: Long): Movie {
        return movieRepository.findById(id).orElseThrow {
            EntityNotFoundException(
                MovieExceptionCodes.MOVIE_NOT_FOUND,
                id
            )
        }
    }

    override fun getByTitle(title: String): Movie {
        return movieRepository.findByTitle(title).orElseThrow {
            ValidationException(
                MovieExceptionCodes.MOVIE_NOT_FOUND,
                title
            )
        }
    }

    override fun getAll(): List<Movie> {
        return movieRepository.findAll()
    }

    @Transactional
    override fun create(dto: MovieDTO): Movie {
        if (movieRepository.existsByTitle(dto.title)) {
            throw ValidationException(MovieExceptionCodes.MOVIE_TITLE_ALREADY_EXIST, dto.title)
        }
        val movie: Movie = movieMapper.toEntity(dto)
        movie.users = dto.userIds.map { id -> userService.getById(id) }.toMutableList()
        movie.createdAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        movieRepository.save(movie)
        return movieRepository.save(movie)
    }

    override fun update(id: Long, dto: MovieDTO): Movie {
        if (movieRepository.existsByTitleAndIdNot(dto.title, id)) {
            throw ValidationException(
                MovieExceptionCodes.MOVIE_TITLE_ALREADY_EXIST,
                dto.title
            )
        }
        val movie: Movie = getById(id)
        movie.updatedAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        movie.users = dto.userIds.map { id -> userService.getById(id) }.toMutableList()
        movie.title = dto.title
        movie.year = dto.year
        movie.genre = dto.genre
        movie.photoPath = dto.photoPath
        return movieRepository.save(movie)
    }

    override fun upload(dto: UploadDTO): Movie{
        val movie = getById(dto.id)
        movie.photoPath = b2ImageService.upload(dto.file)
        return movieRepository.save(movie)
    }

    override fun delete(id: Long){
        movieRepository.deleteById(id)
    }
}