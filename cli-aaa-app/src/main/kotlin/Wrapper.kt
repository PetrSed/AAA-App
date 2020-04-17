import domains.*
import Handler

class Wrapper {
    var users = mutableListOf<User>(
        User(login = "admin", hash = "21232f297a57a5a743894a0e4a801fc3"),
        User(login = "petr", hash = "5685dc8ca490fb3399ed2ddeb5faddca"),
        User(login = "vasya", hash = "202cb962ac59075b964b07152d234b70"),
        User(login = "q", hash = "3a4d92a1200aad406ac50377c7d863aa")
    )
        private set


    var resources = mutableListOf<Resource>(
        Resource(res = "A", login = "vasya", role = Role.valueOf("READ")),
        Resource(res = "A.B", login = "admin", role = Role.valueOf("EXECUTE")),
        Resource(res = "A.B.C", login = "petr", role = Role.valueOf("WRITE")),
        Resource(res = "A.B.D", login = "q", role = Role.valueOf("READ")),
        Resource(res = "A.BB", login = "petr", role = Role.valueOf("EXECUTE")),
        Resource(res = "AB", login = "admin", role = Role.valueOf("WRITE")),
        Resource(res = "A", login = "admin", role = Role.valueOf("READ"))
    )
        private set

    var sessions: MutableList<Session> = mutableListOf()

    fun addSession(session: Session) = sessions.add(session)
    fun loginExists(login: String): Boolean = users.find{it.login == login} != null
    fun getUser(login: String) = users.first { it.login == login }
    fun getAccessResources(login: String): List<Resource> = resources.filter { it.login == login }
    fun hasPermission(login: String, role: Role, prefix: String): Boolean {
        val rights = getAccessResources(login)
        val accessResult = getAccess(rights, prefix)
        val access = accessResult.first
        val roles = accessResult.second.distinct()
        return (access && role in roles)
    }
    }
    private fun checkAccess(needResourceName: String, resourceName: String): Boolean =
        needResourceName.startsWith(resourceName) || needResourceName == resourceName

    private fun getAccess(resources: List<Resource>, needResourceName: String): Pair<Boolean, MutableList<Role>> {
        var access = false
        val roles = mutableListOf<Role>()
        for (resource in resources) {
            val resourceName = resource.res
            if (checkAccess(needResourceName, resourceName)) {
                access = true
                val role = resource.role
                roles.add(role)
            }
        }
        return Pair(access, roles)
    }
