package servlets

import java.io.IOException
import java.io.UnsupportedEncodingException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet(name = "PostServlet", urlPatterns = ["echo/post"])
class PostServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val input = request.getParameter("input")
        response.sendRedirect("/echo/get?id=$input")
    }

}