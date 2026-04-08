package fit.cvut.cz.api.service

import fit.cvut.cz.api.dto.MovieDTO
import fit.cvut.cz.api.dto.UploadDTO
import fit.cvut.cz.api.models.Movie

interface MovieService {

    fun getById(id: Long): Movie

    fun getByTitle(title: String): Movie

    fun getAll(): List<Movie>

    fun create(dto: MovieDTO): Movie

    fun update (id: Long, dto: MovieDTO): Movie

    fun delete(id: Long)

    fun upload(dto: UploadDTO): Movie
}