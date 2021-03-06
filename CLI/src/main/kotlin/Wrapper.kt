import java.io.Closeable
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import org.apache.logging.log4j.LogManager
import org.flywaydb.core.Flyway
import domains.User
import domains.Session

class Wrapper : Closeable {
    private var con: Connection? = null
    private val logger = LogManager.getLogger()

    fun dbExists(): Boolean = File("aaa.h2.db").exists()

    fun getUser(login: String): User? {
        logger.info("Get prepared statement with users")
        val getUser = con!!.prepareStatement("SELECT hash FROM users WHERE login = ?")
        getUser.setString(1, login)
        logger.info("Get result set with user")
        val res = getUser.executeQuery()
        res.next()
        val hash = res.getString("hash")
        logger.info("Close result set with user")
        res.close()
        logger.info("Close prepared statement with users")
        getUser.close()
        return User(login, hash)
    }

    @Suppress("MagicNumber")
    fun hasPermission(login: String, role: String, permissionRegex: String): Boolean {
        logger.info("Get prepared statement with permission")
        val getPermission = con!!.prepareStatement(
            "SELECT count(*) FROM permissions WHERE login = ? and role = ? and res REGEXP ?"
        )
        getPermission.setString(1, login)
        getPermission.setString(2, role)
        logger.info("Matching resources against '$permissionRegex'")
        getPermission.setString(3, permissionRegex)
        logger.info("Get result set with permission")
        val res = getPermission.executeQuery()
        res.next()
        val ans = res.getInt(1) > 0
        logger.info("Close result set with permission")
        res.close()
        logger.info("Close prepared statement with permission")
        getPermission.close()
        return ans
    }

    @Suppress("MagicNumber") // Will be fixed later. Maybe
    fun addSession(activity: Session) {
        logger.info("Get prepared statement with activities")
        val addAct = con!!.prepareStatement(
            "INSERT INTO " +
                    "sessions(login, role, ds, de, vol, res) " +
                    "VALUES (?, ?, ?, ?, ?, ?)"
        )
        addAct.setString(1, activity.login)
        addAct.setString(2, activity.role)
        addAct.setString(3, activity.dateStart)
        addAct.setString(4, activity.dateEnd)
        addAct.setString(5, activity.vol)
        addAct.setString(6, activity.res)
        addAct.execute()
        logger.info("Close prepared statement with activities")
        addAct.close()
    }

    fun loginExists(login: String): Boolean {
        logger.info("Get prepared statement with user")
        val getUser = con!!.prepareStatement("SELECT count(*) FROM users WHERE login = ?")
        getUser.setString(1, login)
        logger.info("Get result set with user")
        val res = getUser.executeQuery()
        res.next()
        val ans = res.getInt(1) > 0
        logger.info("Close result set with user")
        res.close()
        logger.info("Close prepared statement with user")
        getUser.close()
        return ans
    }

    fun initDatabase(url: String, login: String, pass: String) {
        val flyway = Flyway.configure().dataSource("$url;MV_STORE=FALSE", login, pass).locations("filesystem:db").load()
        flyway.migrate()
    }

    fun connect(url: String, login: String, pass: String) {
        logger.info("Сonnecting to database")
        con = DriverManager.getConnection(url, login, pass)
    }

    override fun close() {
        logger.info("Disconnecting from database")
        con?.close()
    }
}