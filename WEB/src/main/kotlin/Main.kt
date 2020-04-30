import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import servlets.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val port = 8080
        val server = Server(port)
        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.contextPath = "/"
        // http://localhost:8080/func
        context.addServlet(ServletHolder(EchoServlet()), "/echo")
        context.addServlet(ServletHolder(GetServlet()), "/get")
        context.addServlet(ServletHolder(PostServlet()), "/post")
        val handlers = HandlerList()
        handlers.handlers = arrayOf<Handler>(context)
        server.handler = handlers
        try {
            server.start()
            println("Listening port : $port")
            server.join()
        } catch (e: Exception) {
            println("Error.")
            e.printStackTrace()
        }
    }
}