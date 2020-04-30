package servlets

import java.io.IOException
import java.io.UnsupportedEncodingException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet("/get")
class GetServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val id = request.getParameter("id")
        response.writer.print("Error 404. Page not found.")
    }
    
}