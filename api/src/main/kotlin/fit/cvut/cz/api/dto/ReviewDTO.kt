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
class ReviewDTO(
    val id: Long,
    var userId: Long,
    var movieId: Long,
    var text: String,
    var photoPath: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)