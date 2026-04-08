package fit.cvut.cz.api.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.util.*

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class MovieDTO (
    var id: Long? = null,
    var title: String,
    var year: Int,
    var genre: List<String>,
    var photoPath: String? = null,
    var userIds: List<Long> = listOf(),
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
)