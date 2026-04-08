package fit.cvut.cz.api.exception

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import fit.cvut.cz.api.exception.exceptionCodes.ExceptionCodes
import lombok.Data
import lombok.EqualsAndHashCode

@EqualsAndHashCode(callSuper = true)
@JsonIncludeProperties("code", "description")
@Data
open class AbstractException(exceptionCode: ExceptionCodes, vararg formatArgs: Any?) : RuntimeException() {
    internal val code: String = exceptionCode.code
    internal val description: String = try {
        val argsToFormat =
            if (formatArgs.size == 1 && formatArgs[0] is Array<*>) {
                (formatArgs[0] as Array<out Any?>)
            } else {
                formatArgs
            }
        exceptionCode.description.format(*argsToFormat)
    } catch (e: Exception) {
        "Formatting error: ${e.message}"
    }
}