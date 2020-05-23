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
@WebServlet(name = "PostServlet", urlPatterns = ["echo/post"])
class PostServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val input = request.getParameter("input")
        response.sendRedirect("get?id=${encode(input, "utf-8")}")
    }

}