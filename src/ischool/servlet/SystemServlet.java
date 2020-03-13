package ischool.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *@GWHJN 跟我回江南
 */
public class SystemServlet extends HttpServlet {


    private static final long serialVersionUID = -6133107428774881992L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resq)
            throws ServletException, IOException {
       doPost(req, resq);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resq)
            throws ServletException, IOException {
        String method = req.getParameter(("method"));
        if("toAdminView".equals(method)){
            req.getRequestDispatcher("view/system.jsp").forward(req,resq);
        }
        if("LoginOut".equals(method)){
            req.getSession().setAttribute("user",null);
            req.getSession().setAttribute("userType",null);
            resq.sendRedirect("index.jsp");
        }
    }
}