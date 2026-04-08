package fit.cvut.cz.api.exception.exceptionCodes

enum class ReviewExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    REVIEW_NOT_FOUND("MVE-RVW-001", "Review with id %d not found"),
    REVIEW_FORBIDDEN("MVE-RVW-002", "Review with id %d is forbidden"),
    USER_CANT_CHANGE_THIS_REVIEW("MVE-RVW-003", "You are not allowed to change this review"),
}