package servlets

import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.net.URLEncoder.encode
import com.google.inject.Singleton


@Singleton
class ActivityServlet : HttpServlet() {
    override fun service(request: HttpServletRequest, response: HttpServletResponse) {
        response.writer.print("Hello, world!")
    }


}