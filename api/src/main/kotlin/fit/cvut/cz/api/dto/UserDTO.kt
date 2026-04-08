package fit.cvut.cz.api.dto

import fit.cvut.cz.api.models.Role
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.util.*

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserDTO(
    var id: Long? = null,
    var username: String,
    var password: String,
    var profilePhoto: String? = null,
    var movieIds: List<Long> = listOf(),
    var role: Role? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)