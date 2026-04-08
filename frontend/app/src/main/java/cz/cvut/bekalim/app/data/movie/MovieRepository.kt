package cz.cvut.bekalim.app.data.movie

class MovieRepository(private val api: MovieApi) {
    suspend fun fetchAll()   = runCatching { api.getAll() }
    suspend fun fetchOne(id: Long) = runCatching { api.getOne(id) }
}