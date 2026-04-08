package fit.cvut.cz.api.security

import fit.cvut.cz.api.dto.AuthRequest
import fit.cvut.cz.api.dto.LoginResponse
import fit.cvut.cz.api.dto.RegisterResponse
import fit.cvut.cz.api.dto.UserDTO
import fit.cvut.cz.api.exception.ValidationException
import fit.cvut.cz.api.exception.exceptionCodes.UserExceptionCodes
import fit.cvut.cz.api.mapper.UserMapper
import fit.cvut.cz.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//@RestController
//@RequestMapping("/api/auth")
//class AuthController(
//    private val userRepo: UserService,
//    private val jwtUtils: JwtUtils,
//    private val userMapper: UserMapper,
//) {
//    private val encoder = BCryptPasswordEncoder()
//
//    @PostMapping("/register")
//    fun register(@RequestBody req: AuthRequest): UserDTO {
////        if (userRepo.getByUsername(dto.username)) {
////            return ResponseEntity.badRequest().build()
////        }
//        val dto = UserDTO(
//            username = req.login!!,
//            password = req.password!!,
//            movieIds = mutableListOf(),
//        )
//        val user = userRepo.create(dto)
//        return userMapper.toDto(user)
//    }
//
//    @PostMapping("/login")
//    fun login(@RequestBody req: AuthRequest): Map<String, String> {
//        val dto = UserDTO(
//            username = req.login!!,
//            password = req.password!!,
//            movieIds = mutableListOf(),
//        )
//        val user = userRepo.getByUsername(dto.username)
//        return if (dto.password == user.password) {
//            val token = jwtUtils.generateJwtToken(user.username!!)
//            mapOf("token" to token)
//        } else mapOf()
//    }
//}

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtUtils: JwtUtils,
    private val userMapper: UserMapper,
) {
    private val encoder = BCryptPasswordEncoder()

    @PostMapping("/register")
    fun register(@RequestBody req: AuthRequest): UserDTO {
        val dto = UserDTO(
            username = req.login!!,
            password = req.password!!,
            movieIds = emptyList()
        )
        return userMapper.toDto(userService.create(dto))
    }

    @PostMapping("/login")
    fun login(@RequestBody req: AuthRequest): LoginResponse {
        val user = userService.getByUsername(req.login!!)
        if (req.password != user.password) {
            throw ValidationException(UserExceptionCodes.USER_NOT_FOUND, req.login)
        }
        val token = jwtUtils.generateJwtToken(user.username!!)
        return LoginResponse(token = token)
    }
}
