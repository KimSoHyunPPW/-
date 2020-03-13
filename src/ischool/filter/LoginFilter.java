package ischool.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public  class LoginFilter implements Filter {
    public void destory()
    {

    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        Object user = request.getSession().getAttribute("user");
        if(user == null){
            System.out.println("未登录,重定向到登陆页面");
            response.sendRedirect("/index.jsp");
            return;
        }else{
            chain.doFilter(req,resp);

        }


    }

    public void init(FilterConfig arg0) throws  ServletException{

    }


    @Override
    public boolean isLoggable(LogRecord record) {
        return false;
    }
}
