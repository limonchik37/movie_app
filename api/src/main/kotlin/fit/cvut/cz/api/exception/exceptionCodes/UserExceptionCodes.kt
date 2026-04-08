package fit.cvut.cz.api.exception.exceptionCodes

enum class UserExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    USER_NOT_FOUND("MVE-USR-001", "User with id %d not found"),
    USER_USERNAME_ALREADY_EXISTS("MVE-USR-002", "User username %s already exists"),
    USER_PASSWORD_IS_EMPTY("MVE-USR-003", "User password is empty"),
    USER_DOES_NOT_EXISTS("MVE-USR-004", "User does not exists"),
}