package fit.cvut.cz.api.service

import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.dto.UserDTO
import fit.cvut.cz.api.models.User

interface UserService {

    fun getAll(): List<User>

    fun getById(id: Long): User

    fun create(dto: UserDTO): User

    fun getByUsername(username: String): User

    fun update(id: Long, dto: UserDTO): User

    fun delete(id: Long)

    fun upload(dto: UploadDTO): User
}