package servlets

import java.io.IOException
import java.net.URLEncoder.encode
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet(name = "GetServlet", urlPatterns = ["echo/get"])
class GetServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val id = request.getParameter("id")
        request.setAttribute("id", id)
        request.getRequestDispatcher("../response.jsp").forward(request, response)
    }
    
}