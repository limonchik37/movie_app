package fit.cvut.cz.api.controller

import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.dto.UploadType
import fit.cvut.cz.api.service.MovieService
import fit.cvut.cz.api.service.ReviewService
import fit.cvut.cz.api.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/image")
class B2ImageController(
    private val userService: UserService,
    private val movieService: MovieService,
    private val reviewService: ReviewService
) {

    @PostMapping
    fun upload(@ModelAttribute dto: UploadDTO): ResponseEntity<*> { // TODO separate endpoints
        println("Received upload request: id=${dto.id}, type=${dto.type}, file=${dto.file.originalFilename}")
        return when (dto.type) {
            UploadType.USER -> ResponseEntity.ok(userService.upload(dto))
            UploadType.MOVIE -> ResponseEntity.ok(movieService.upload(dto))
            UploadType.REVIEW -> ResponseEntity.ok(reviewService.upload(dto))
        }
    }

}