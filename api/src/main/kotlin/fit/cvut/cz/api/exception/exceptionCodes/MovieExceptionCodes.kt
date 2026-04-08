package fit.cvut.cz.api.exception.exceptionCodes

enum class MovieExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    MOVIE_NOT_FOUND("MVE-MOV-001", "Movie with id %d not found"),
    MOVIE_DOES_NOT_EXISTS("MVE-MOV-002", "Movie with title %s does not exists"),
    MOVIE_TITLE_ALREADY_EXIST("MVE-MOV-003", "Movie with title %s already exists"),
}