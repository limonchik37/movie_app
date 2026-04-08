package fit.cvut.cz.api.repository

import fit.cvut.cz.api.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): Optional<User>
    fun existsByUsernameAndIdNot(username: String, id: Long): Boolean
}