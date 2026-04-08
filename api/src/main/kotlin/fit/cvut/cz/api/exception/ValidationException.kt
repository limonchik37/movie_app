package fit.cvut.cz.api.exception

import fit.cvut.cz.api.exception.exceptionCodes.ExceptionCodes

class ValidationException(
    exceptionCode: ExceptionCodes,
    vararg formatArgs: Any?
) : AbstractException(exceptionCode, formatArgs)