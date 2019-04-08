package pl.ife.tcs.basicclientservice.handler

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.ife.tcs.basicclientservice.controller.BasicClientController
import java.util.logging.Logger

@ControllerAdvice
class ExceptionHandler{

    private val logger: Logger = Logger.getLogger(BasicClientController::class.simpleName)

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception) {
        logger.warning("Exception: $exception")
    }
}