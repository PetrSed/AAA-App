package configs

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.servlet.GuiceServletContextListener
import com.google.inject.servlet.ServletModule
import servlets.EchoServlet
import servlets.PostServlet
import servlets.GetServlet
import servlets.UserServlet
import servlets.ActivityServlet
import servlets.AuthorityServlet

class ServletsConfig() : GuiceServletContextListener() {
    override fun getInjector(): Injector = Guice.createInjector(object : ServletModule() {
        override fun configureServlets() {
            serve("/echo/get").with(GetServlet::class.java)
            serve("/echo/post").with(PostServlet::class.java)
            serve("/echo/*").with(EchoServlet::class.java)
            serve("/ajax/user").with(UserServlet::class.java)
            serve("/ajax/authority").with(AuthorityServlet::class.java)
            serve("/ajax/activity").with(ActivityServlet::class.java)
        }
    })
}
