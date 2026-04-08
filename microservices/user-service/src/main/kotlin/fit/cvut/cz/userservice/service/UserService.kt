package fit.cvut.cz.userservice.service

import fit.cvut.cz.userservice.dto.UpdateUserRequest
import fit.cvut.cz.userservice.dto.UserDTO
import fit.cvut.cz.userservice.kafka.UserEventProducer
import fit.cvut.cz.userservice.kafka.UserRegisteredEvent
import fit.cvut.cz.userservice.model.User
import fit.cvut.cz.userservice.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userEventProducer: UserEventProducer
) {
    private val encoder = BCryptPasswordEncoder()

    fun getAll(): List<UserDTO> = userRepository.findAll().map { it.toDto() }

    fun getById(id: Long): UserDTO = userRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User $id not found") }
        .toDto()

    fun getEntityByUsername(username: String): User = userRepository.findByUsername(username)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User $username not found") }

    @Transactional
    fun register(username: String, rawPassword: String): UserDTO {
        if (userRepository.existsByUsername(username)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Username '$username' already taken")
        }
        val user = User(
            username = username,
            password = encoder.encode(rawPassword)
        )
        val saved = userRepository.save(user)

        // Publish Kafka event
        userEventProducer.publishUserRegistered(
            UserRegisteredEvent(userId = saved.id!!, username = saved.username)
        )

        return saved.toDto()
    }

    fun validateCredentials(username: String, rawPassword: String): User {
        val user = getEntityByUsername(username)
        if (!encoder.matches(rawPassword, user.password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
        return user
    }

    @Transactional
    fun update(id: Long, req: UpdateUserRequest): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User $id not found") }
        req.username?.let { user.username = it }
        req.profilePhoto?.let { user.profilePhoto = it }
        return userRepository.save(user).toDto()
    }

    @Transactional
    fun delete(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User $id not found")
        }
        userRepository.deleteById(id)
    }

    private fun User.toDto() = UserDTO(
        id = id,
        username = username,
        profilePhoto = profilePhoto,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
