package fit.cvut.cz.api.security

import fit.cvut.cz.api.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val repo: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        repo.findByUsername(username)
            .map { CustomUserDetails(it) }
            .orElseThrow { UsernameNotFoundException("User '$username' not found") }
}