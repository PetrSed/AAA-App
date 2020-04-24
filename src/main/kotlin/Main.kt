import org.apache.logging.log4j.LogManager
import kotlin.system.exitProcess
import ExitCodes.*

fun main(args: Array<String>){
    val app = App()
    val exitCode = app.start(args)
    exitProcess(exitCode)
}