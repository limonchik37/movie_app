package fit.cvut.cz.movieservice.controller

import fit.cvut.cz.movieservice.dto.CreateMovieRequest
import fit.cvut.cz.movieservice.dto.MovieDTO
import fit.cvut.cz.movieservice.service.MovieService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/movies")
class MovieController(private val movieService: MovieService) {

    @GetMapping
    fun getAll(): List<MovieDTO> = movieService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): MovieDTO = movieService.getById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: CreateMovieRequest): MovieDTO = movieService.create(req)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody req: CreateMovieRequest
    ): MovieDTO = movieService.update(id, req)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = movieService.delete(id)
}
