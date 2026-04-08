package fit.cvut.cz.api.util

import fit.cvut.cz.api.exception.ValidationException
import jakarta.persistence.EntityNotFoundException
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Slf4j
@ControllerAdvice
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(value = [Exception::class])
    protected fun handleException(exception: Exception): ResponseEntity<*> {
        log.error(exception.message)
        return ResponseEntity<Any>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    protected fun handleEntityNotFound(exception: EntityNotFoundException): ResponseEntity<*> {
        log.error(exception.message)
        return ResponseEntity<Any>(HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [ValidationException::class])
    protected fun handleValidationException(exception: ValidationException): ResponseEntity<*> {
        log.error(exception.message)
        return ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
    }
}