package fit.cvut.cz.api.security

import fit.cvut.cz.api.models.User
import fit.cvut.cz.api.repository.UserRepository
import fit.cvut.cz.api.service.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Suppress("AnnotationArgumentMustBeConstant")
@Component
class JwtUtils(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration-ms}") private val jwtExpirationMs: Long,
    private val userService: UserService
) {
    fun generateJwtToken(username: String): String = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(Date())
        .setExpiration(Date(Date().time + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .claim("userId", userService.getByUsername(username).id )
        .compact()

    fun getUsernameFromJwt(token: String): String =
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject

    fun validateJwtToken(token: String): Boolean = try {
        Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
        true
    } catch (e: JwtException) {
        false
    }

    fun getAccessClaimsFromJwt(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret)
            .build()
            .parseClaimsJws(token)
            .body
    }
}