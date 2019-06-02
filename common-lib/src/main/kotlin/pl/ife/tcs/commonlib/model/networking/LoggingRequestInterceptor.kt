package pl.ife.tcs.commonlib.model.networking

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.File
import java.io.FileWriter

open class LoggingRequestInterceptor(
        private val tableLength: Int,
        private val entityWidth: Int,
        private val entityUpdateBatch: Int,
        private val collectionUpdateBatch: Int
) : ClientHttpRequestInterceptor {

    private val separator = "========================= {} ========================="

    private val logger = LoggerFactory.getLogger(this::class.java)!!

    private val filename
        get() = "L${tableLength}W${entityWidth}E${collectionUpdateBatch}A${entityUpdateBatch}S${missedCycles}_log${if (isInit)"_INIT" else ""}"

    private var isInit: Boolean = false

    private var missedCycles: Int = 0

    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        val csvData = mutableListOf<String>()
        logRequest(request).apply { csvData.addAll(this) }
        val start = System.currentTimeMillis()
        val response = execution.execute(request, body)
        val executionTime = System.currentTimeMillis() - start
        logResponse(response, executionTime).apply { csvData.addAll(this) }
        printCsvLog(csvData)
        return response
    }

    private fun logRequest(request: HttpRequest): List<String> {
        val csvData = mutableListOf<String>()
        with(logger) {
            info(separator, "Request Begin")
            info("URI            : {}", request.uri.apply { csvData.add(this.toString()) })
            info("Method         : {}", request.method.apply { csvData.add(this.toString()) })
            info("Size           : {}", request.headers.contentLength.apply { csvData.add(this.toString()) })
            info("Headers        : {}", request.headers.apply { csvData.add(this.toString()) })
            info(separator, "Request End")
        }
        isInit = request.headers["init"]?.first()?.toBoolean() ?: false
        missedCycles = request.headers["missedCycles"]?.first()?.toInt() ?: 0
        return csvData
    }

    private fun logResponse(response: ClientHttpResponse, executionTime: Long): List<String> {
        val csvData = mutableListOf<String>()
        with(logger) {
            info(separator, "Response Begin")
            info("Status Code    : {}", response.statusCode.apply { csvData.add(this.toString()) })
            info("Execution Time : {}", executionTime.apply { csvData.add(this.toString()) })
            info("Byte Size      : {}",
                    (if (response.statusCode.is2xxSuccessful) response.body.readBytes().size.toString() else "-").apply { csvData.add(this) })
            info("Headers        : {}", response.headers.apply { csvData.add(this.toString()) })
            info(separator, "Response End")
        }
        return csvData
    }

    private fun printCsvLog( csvData: List<String>) {
        val file = File("/opt/exports/$filename.csv")
        val filePreexists = file.exists()
        var fileWriter: FileWriter? = null
        var csvPrinter: CSVPrinter? = null
        try {
            fileWriter = FileWriter(file, true)
            csvPrinter = CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(
                    "URI", "Method", "Request Size", "Request Haders",
                    "Response Status", "Execution [ms]", "Response Size", "Response Header")
                    .withSkipHeaderRecord(filePreexists)
                    .withAllowMissingColumnNames(true)
                    .withIgnoreEmptyLines(true))
            csvPrinter.printRecord(csvData)
            logger.info("Wrote to CSV successfully")
        } catch (e: Exception) {
            logger.error("Error while writing to CSV: ${e.stackTrace}")
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
                csvPrinter!!.flush()
            } catch (e: Exception) {
                logger.error("Error while closing / flushing file writers: ${e.stackTrace}")
            }
        }
    }
}
