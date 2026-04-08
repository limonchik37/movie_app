package fit.cvut.cz.userservice.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtils(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration-ms}") private val jwtExpirationMs: Long
) {
    fun generateToken(userId: Long, username: String): String = Jwts.builder()
        .setSubject(username)
        .claim("userId", userId)
        .setIssuedAt(Date())
        .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact()

    fun getUsernameFromToken(token: String): String =
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject

    fun getUserIdFromToken(token: String): Long =
        (Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body["userId"] as Int).toLong()

    fun validateToken(token: String): Boolean = try {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
        true
    } catch (e: JwtException) {
        false
    }
}
