package fit.cvut.cz.userservice.controller

import fit.cvut.cz.userservice.dto.UpdateUserRequest
import fit.cvut.cz.userservice.dto.UserDTO
import fit.cvut.cz.userservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAll(): List<UserDTO> = userService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): UserDTO = userService.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody req: UpdateUserRequest
    ): UserDTO = userService.update(id, req)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = userService.delete(id)
}
