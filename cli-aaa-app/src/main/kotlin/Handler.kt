import kotlin.system.exitProcess
import ExitCodes.*
import services.HelpService

class Handler(args: Array<String>) {
    var login = ""
        private set
    var password = ""
        private set
    var role = ""
        private set
    var resource = ""
        private set
    var dateStart = ""
        private set
    var dateEnd = ""
        private set
    var volume = ""
        private set

    init {
        when {
            args.isEmpty() -> terminate(true, HelpCode.code)
            args.contains("-h") || args[0] == "" -> terminate(true, HelpCode.code)
            !args.contains("-h") && !args.contains("-login") -> terminate(true, SuccessCode.code)
            args.size != 4 && args.size != 6 && args.size != 8 && args.size != 14 -> terminate(true, SuccessCode.code)
        }
        for (arg in args) {
            try {
                when (arg) {
                    "-login" -> login = args[args.indexOf(arg) + 1]
                    "-pass" -> password = args[args.indexOf(arg) + 1]
                    "-role" -> role = args[args.indexOf(arg) + 1]
                    "-res" -> resource = args[args.indexOf(arg) + 1]
                    "-ds" -> dateStart = args[args.indexOf(arg) + 1]
                    "-de" -> dateEnd = args[args.indexOf(arg) + 1]
                    "-vol" -> volume = args[args.indexOf(arg) + 1]
                }
            } catch (e: IndexOutOfBoundsException) {
                terminate(true, SuccessCode.code)
            }
        }
        if (role != "" && !(possibleRoles.contains(role))) {
            exitProcess(UnknownRole.code)
        }
    }

    fun autenticationNeeded(): Boolean {
        return login != "" && password != ""
    }

    fun authorizationNeeded(): Boolean {
        return autenticationNeeded() && resource != "" && password != ""
    }

    fun accountingNeeded(): Boolean {
        return authorizationNeeded() && dateEnd != "" && dateStart != "" && volume != ""
    }

    private fun terminate(printHelp: Boolean, errorCode: Int) {
        if (printHelp) {
            val helpService = HelpService()
            helpService.printHelp()
        }
        exitProcess(errorCode)
    }
}

val possibleRoles: Array<String> = arrayOf("WRITE", "READ", "EXECUTE")