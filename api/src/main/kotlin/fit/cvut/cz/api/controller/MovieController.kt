package fit.cvut.cz.api.controller

import fit.cvut.cz.api.dto.MovieDTO
import fit.cvut.cz.api.mapper.MovieMapper
import fit.cvut.cz.api.models.Movie
import fit.cvut.cz.api.service.MovieService
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
@RequestMapping("/movies")
class MovieController(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper
) {

    @GetMapping
    fun getAll(): List<MovieDTO> =
        movieService.getAll().map { movieMapper.toDto(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<MovieDTO> =
        movieService.getById(id).let {
            ResponseEntity.ok(movieMapper.toDto(it))
        }

    @PostMapping
    fun create(@RequestBody dto: MovieDTO): ResponseEntity<MovieDTO> {
        val created = movieService.create(dto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(movieMapper.toDto(created))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: MovieDTO
    ): ResponseEntity<MovieDTO> {
        val updated = movieService.update(id, dto)
        return ResponseEntity.ok(movieMapper.toDto(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        movieService.delete(id)
        return ResponseEntity.noContent().build()
    }
}