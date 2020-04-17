import services.*
import Wrapper
import Handler
import ExitCodes.*
import domains.Resource
import domains.Role
import domains.Session

class App {
    fun start(args: Array<String>): Int {
        var exitCode: Int
        val helpService = HelpService()
        val handler = Handler(args)
        val wrapper = Wrapper()

        exitCode = authentication(helpService, handler, wrapper)
        if (exitCode != -1) return exitCode

        exitCode = authorization(helpService, handler, wrapper)
        if (exitCode != -1) return exitCode

        exitCode = accounting(helpService, handler, wrapper)
        if (exitCode != -1) return exitCode

        return SuccessCode.code
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
        return accountingService.accounting(Session(handler.dateEnd, handler.dateStart, handler.volume, handler.resource))
    }

}