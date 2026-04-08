package fit.cvut.cz.api.mapper

import fit.cvut.cz.api.dto.MovieDTO
import fit.cvut.cz.api.models.Movie
import org.springframework.stereotype.Component

@Component
class MovieMapper {
    fun toDto(entity: Movie): MovieDTO = MovieDTO(
        id = entity.id,
        title = entity.title,
        year = entity.year!!,
        genre = entity.genre,
        photoPath = entity.photoPath,
        userIds = entity.users.mapNotNull { it.id },
        updatedAt = entity.updatedAt,
        createdAt = entity.createdAt
    )

    fun toEntity(dto: MovieDTO): Movie = Movie().apply {
        id = dto.id
        title = dto.title
        year = dto.year
        genre = dto.genre
        photoPath = dto.photoPath
        updatedAt = dto.updatedAt
        createdAt = dto.createdAt
    }
}
