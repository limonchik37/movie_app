package fit.cvut.cz.api.mapper

import fit.cvut.cz.api.dto.ReviewDTO
import fit.cvut.cz.api.models.Review
import org.springframework.stereotype.Component

//@Component
//class ReviewMapper {
//    fun toDto(entity: Review): ReviewDTO = ReviewDTO(
//        id = entity.id!!,
//        userId = entity.user?.id ?: throw IllegalStateException("Review.user must not be null"),
//        movieId = entity.movie?.id ?: throw IllegalStateException("Review.movie must not be null"),
//        text = entity.text ?: "",
//        photoPath = entity.photoPath,
//        updatedAt = entity.updatedAt,
//        createdAt = entity.createdAt,
//    )
//
//    fun toEntity(dto: ReviewDTO): Review = Review().apply {
//        id = dto.id
//        text = dto.text
//        photoPath = dto.photoPath
//        updatedAt = dto.updatedAt
//        createdAt = dto.createdAt
//        // user и movie устанавливаются в сервисе
//    }
//}


@Component
class ReviewMapper {

    fun toDto(entity: Review): ReviewDTO = ReviewDTO(
        id        = entity.id!!,
        userId    = entity.user?.id ?: throw IllegalStateException("Review.user must not be null"),
        movieId   = entity.movie?.id ?: throw IllegalStateException("Review.movie must not be null"),
        text      = entity.text ?: "",
        photoPath = entity.photoPath,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(dto: ReviewDTO): Review = Review().apply {
        // НЕ КОПИРУЕМ сюда id, createdAt, updatedAt!
        text      = dto.text
        photoPath = dto.photoPath
        // user и movie проставляются в сервисе, а койтлин‑ датe‑поля оставляем null,
        // чтобы JPA Auditing сам заполнил createdAt/updatedAt
    }
}