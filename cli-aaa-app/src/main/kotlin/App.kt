import services.*
import Wrapper
import Handler
import ExitCodes.*
import domains.Resource
import domains.Role
import domains.Session
import org.apache.logging.log4j.LogManager


class App {
    val logger = LogManager.getLogger()
    private fun logArgs(handler: Handler) {
        if (handler.login != "") logger.info("Login = ${handler.login}")
        if (handler.password != "") logger.info("Pass = ${handler.password}")
        if (handler.resource != "") logger.info("Res = ${handler.resource}")
        if (handler.role != "") logger.info("Role = ${handler.role}")
        if (handler.dateStart != "") logger.info("Ds = ${handler.dateStart}")
        if (handler.dateEnd != "") logger.info("De = ${handler.dateEnd}")
        if (handler.volume != "") logger.info("Vol = ${handler.volume}")
    }

    fun start(args: Array<String>): Int {
        logger.info("Start program")
        var exitCode: Int
        val helpService = HelpService()
        val wrapper = Wrapper()
        if (wrapper.dbExists()) logger.info("Using existing database.")
        else logger.warn("Database does not exist. Initiating new database.")
        logger.info("Attempting database migration.")
        wrapper.initDatabase(
            System.getenv("H2_URL") ?: "jdbc:h2:./aaa",
            System.getenv("H2_LOGIN") ?: "se",
            System.getenv("H2_PASS") ?: ""
        )

        wrapper.connect(
            System.getenv("H2_URL") ?: "jdbc:h2:./aaa",
            System.getenv("H2_LOGIN") ?: "se",
            System.getenv("H2_PASS") ?: ""
        )

        return wrapper.use<Wrapper, Int> {
            val handler = Handler(args)
            logArgs(handler)
            if (!handler.accountingNeeded() || !handler.autenticationNeeded() || !handler.accountingNeeded()) {
                logger.info("Arguments were not passed. Print help. Exit.")
                helpService.printHelp()
                return@use HelpCode.code
            }

            exitCode = authentication(helpService, handler, wrapper)
            if (exitCode != -1) return@use exitCode

            exitCode = authorization(helpService, handler, wrapper)
            if (exitCode != -1) return@use exitCode

            exitCode = accounting(helpService, handler, wrapper)
            if (exitCode != -1) return@use exitCode

            logger.info("Success. Exit.")
            return@use SuccessCode.code
        }
    }

    fun authentication(helpService: HelpService, handler: Handler, wrapper: Wrapper): Int {
        val autenticationService = AuthenticationService(wrapper)
        if (!handler.autenticationNeeded()) {
            helpService.printHelp()
            return HelpCode.code
        }
        return autenticationService.authenticate(handler.login, handler.password)
    }

    fun authorization(helpService: HelpService, handler: Handler, wrapper: Wrapper): Int {
        val authorizationService = AuthorizationService(wrapper)
        if (!handler.authorizationNeeded()) {
            helpService.printHelp()
            return HelpCode.code
        }
        return authorizationService.authorize(Resource(handler.login, Role.valueOf(handler.role), handler.resource))
    }

    fun accounting(helpService: HelpService, handler: Handler, wrapper: Wrapper): Int {
        val accountingService = AccountingService(wrapper)
        if (!handler.accountingNeeded()) {
            helpService.printHelp()
            return HelpCode.code
        }
        return accountingService.accounting(
            Session(
                handler.login,
                handler.role,
                handler.dateEnd,
                handler.dateStart,
                handler.volume,
                handler.resource
            )
        )
    }

}