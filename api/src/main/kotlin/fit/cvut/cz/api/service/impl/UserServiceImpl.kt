package fit.cvut.cz.api.service.impl

import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.dto.UserDTO
import fit.cvut.cz.api.exception.EntityNotFoundException
import fit.cvut.cz.api.exception.ValidationException
import fit.cvut.cz.api.exception.exceptionCodes.UserExceptionCodes
import fit.cvut.cz.api.mapper.UserMapper
import fit.cvut.cz.api.models.Role
import fit.cvut.cz.api.models.User
import fit.cvut.cz.api.repository.UserRepository
import fit.cvut.cz.api.service.MovieService
import fit.cvut.cz.api.service.UserService
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    @Lazy private val movieService: MovieService,
    private val b2ImageService: B2ImageService
) : UserService {
    @Transactional
    override fun getAll(): List<User> =
        userRepository.findAll()

    @Transactional
    override fun getById(id: Long): User {
        val user: User = userRepository.findById(id).orElseThrow {
            EntityNotFoundException(UserExceptionCodes.USER_NOT_FOUND, id)
        }
        return user
    }

    @Transactional
    override fun getByUsername(username: String): User {
        val user: User = userRepository.findByUsername(username).orElseThrow {
            EntityNotFoundException(UserExceptionCodes.USER_NOT_FOUND, username)
        }
        return user
    }

    @Transactional
    override fun create(dto: UserDTO): User {
        if (dto.username.isBlank() || userRepository.existsByUsername(dto.username)) {
            throw ValidationException(
                UserExceptionCodes.USER_USERNAME_ALREADY_EXISTS,
                dto.username
            )
        }
        // dto.createdAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        val user: User = userMapper.toEntity(dto)
        user.movies = dto.movieIds.map { movieService.getById(it) }.toMutableList()
        user.role = Role.ROLE_USER
        val now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        user.createdAt = now
        user.updatedAt = now
        return userRepository.save(user)
    }

    @Transactional
    override fun update(id: Long, dto: UserDTO): User {
        if (userRepository.existsByUsernameAndIdNot(dto.username, id)) {
            throw ValidationException(
                UserExceptionCodes.USER_USERNAME_ALREADY_EXISTS,
                dto.username
            )
        }
        val user: User = userRepository.findById(id).orElseThrow {
            EntityNotFoundException(UserExceptionCodes.USER_NOT_FOUND, id)
        }
        user.updatedAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        user.username = dto.username
        user.password = dto.password
        user.profilePhoto = dto.profilePhoto
        user.movies = dto.movieIds.map { movieService.getById(it) }.toMutableList()
        val updated: User = userRepository.save(user)
        return updated
    }

    override fun upload(dto: UploadDTO): User {
        val user = getById(dto.id)
        user.profilePhoto = b2ImageService.upload(dto.file)
        return userRepository.save(user)
    }

    @Transactional
    override fun delete(id: Long) {
        if (!userRepository.existsById(id)) {
            throw EntityNotFoundException(UserExceptionCodes.USER_NOT_FOUND, id)
        }
        userRepository.deleteById(id)
    }
}
