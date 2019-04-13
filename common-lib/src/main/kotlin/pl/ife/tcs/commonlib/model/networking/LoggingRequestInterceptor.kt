package pl.ife.tcs.commonlib.model.networking

import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class LoggingRequestInterceptor: ClientHttpRequestInterceptor {

    private val separator = "========================= {} ========================="

    private val logger = LoggerFactory.getLogger(this::class.java)!!

    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        logRequest(request)
        val start = System.currentTimeMillis()
        val response = execution.execute(request, body)
        val executionTime = System.currentTimeMillis() - start
        logResponse(response, executionTime)
        return response
    }

    private fun logRequest(request: HttpRequest) {
        with(logger) {
            info(separator, "Request Begin")
            info("URI            : {}", request.uri)
            info("Method         : {}", request.method)
            info("Size           : {}", request.headers.contentLength)
            info("Headers        : {}", request.headers)
            info(separator, "Request End")
        }
    }

    private fun logResponse(response: ClientHttpResponse, executionTime: Long) {
        with(logger) {
            info(separator, "Response Begin")
            info("Status Code    : {}", response.statusCode)
            info("Execution Time : {}", executionTime)
            info("Byte Size      : {}", if (response.statusCode.is2xxSuccessful) response.body.readBytes().size else "-")
            info("Headers        : {}", response.headers)
            info(separator, "Response End")
        }
    }
}