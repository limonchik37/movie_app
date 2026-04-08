package fit.cvut.cz.api.security

import fit.cvut.cz.api.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthTokenFilter(
    private val jwtUtils: JwtUtils,
    private val userService: UserService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
        if (jwt != null && jwtUtils.validateJwtToken(jwt!!)) {
            val claims = jwtUtils.getAccessClaimsFromJwt(jwt)
            val userId = claims.get("userId", Integer::class.java).toLong()
            val user = userService.getById(userId)
            val auth = JwtAuthentication()
            auth.userId = user.id
            auth.login = user.username!!
            auth.role = user.role!!
            auth.isAuthenticated = true
            SecurityContextHolder.getContext().authentication = auth
        }
        filterChain.doFilter(request, response)
    }
}