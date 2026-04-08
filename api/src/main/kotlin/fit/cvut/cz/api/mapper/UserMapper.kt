package fit.cvut.cz.api.mapper

import fit.cvut.cz.api.dto.UserDTO
import fit.cvut.cz.api.models.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDto(entity: User): UserDTO = UserDTO(
        id = entity.id,
        username = entity.username ?: "",
        password = entity.password ?: "",
        profilePhoto = entity.profilePhoto,
        movieIds = entity.movies.mapNotNull { it.id },
        updatedAt = entity.updatedAt,
        createdAt = entity.createdAt

    )

    fun toEntity(dto: UserDTO): User = User().apply {
        id = dto.id
        username = dto.username
        password = dto.password
        profilePhoto = dto.profilePhoto
        // updatedAt = dto.updatedAt
        // createdAt = dto.createdAt
        // movies устанавливаются в сервисе
    }
}
