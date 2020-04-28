package services

import Wrapper
import domains.*
import ExitCodes.*

class AuthorizationService(private val wrapper: Wrapper) {
    fun authorize(resource: Resource): Int {
        var resRegex = resource.res
        while (resRegex.contains(Regex("(?<=[A-Z])(\\.[A-Z]+[^)\\s]*)")))
            resRegex = resRegex.replace(Regex("(?<=[A-Z])(\\.[A-Z]+[^)\\s]*)"), "(\\\\$1)?")
        resRegex = "^$resRegex$"
        if (wrapper.hasPermission(resource.login, resource.role.toString(), resRegex)) return -1 else return NoAccess.code
    }
}
