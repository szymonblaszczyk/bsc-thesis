package pl.ife.tcs.diffclientservice.handler

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.logging.Logger

@ControllerAdvice
class ServiceExceptionHandler{

    private val logger: Logger = Logger.getLogger(ServiceExceptionHandler::class.simpleName)

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception) {
        logger.warning("Exception: $exception")
    }
}