package domains

import java.time.LocalDate
import kotlin.system.exitProcess
import ExitCodes.*

data class Session(
    val login: String,
    val role: String,
    val dateStart: String,
    val dateEnd: String,
    val vol: String,
    val res: String
) {
    var formattedDateStart: LocalDate = LocalDate.now()
        private set
    var formattedDateEnd: LocalDate = LocalDate.now()
        private set
    var volume: Int = 0
        private set

    init {
        try {
            formattedDateStart = LocalDate.parse(dateStart)
            formattedDateEnd = LocalDate.parse(dateEnd)
        } catch (e: java.time.format.DateTimeParseException) {
            exitProcess(IncorrectActivity.code)
        }
        if (formattedDateStart.isAfter(formattedDateEnd)) {
            exitProcess(IncorrectActivity.code)
        }
        try {
            volume = vol.toInt()
        } catch (e: NumberFormatException) {
            exitProcess(IncorrectActivity.code)
        }
    }
}