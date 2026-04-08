package fit.cvut.cz.userservice.controller

import fit.cvut.cz.userservice.dto.AuthRequest
import fit.cvut.cz.userservice.dto.LoginResponse
import fit.cvut.cz.userservice.dto.UserDTO
import fit.cvut.cz.userservice.security.JwtUtils
import fit.cvut.cz.userservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtUtils: JwtUtils
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody req: AuthRequest): UserDTO =
        userService.register(req.login, req.password)

    @PostMapping("/login")
    fun login(@RequestBody req: AuthRequest): LoginResponse {
        val user = userService.validateCredentials(req.login, req.password)
        val token = jwtUtils.generateToken(user.id!!, user.username)
        return LoginResponse(token = token, userId = user.id!!, username = user.username)
    }
}
