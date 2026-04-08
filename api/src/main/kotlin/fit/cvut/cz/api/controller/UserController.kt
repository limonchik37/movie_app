package fit.cvut.cz.api.controller

import fit.cvut.cz.api.dto.UserDTO
import fit.cvut.cz.api.mapper.UserMapper
import fit.cvut.cz.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {

    @GetMapping
    fun getAll(): List<UserDTO> =
        userService.getAll().map { userMapper.toDto(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserDTO> =
        userService.getById(id).let {
            ResponseEntity.ok(userMapper.toDto(it))
        }

    @PostMapping
    fun create(@RequestBody dto: UserDTO): ResponseEntity<UserDTO> {
        val created = userService.create(dto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userMapper.toDto(created))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: UserDTO,
    ): ResponseEntity<UserDTO> {
        val updated = userService.update(id, dto)
        return ResponseEntity.ok(userMapper.toDto(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}