package ischool.servlet;


import ischool.util.AdminDao;
import ischool.entity.Admin;
import ischool.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录
 * @GWHJN 跟我回江南
 *
 */
public class LoginServlet extends HttpServlet {


    private static final long serialVersionUID = 8394701639528073321L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String msg = "success";
        if (StringUtil.isEmpty(name)) {
            msg = "用户名不能为空！";
        }
        if (StringUtil.isEmpty(password)) {
            msg = "密码不能为空！";
        }
        if ("success".equals(msg)) {
            String typeString = req.getParameter("type");
            try {
                int type = Integer.parseInt(typeString);
                if (type == 1) {
                    //超级管理员用户
                    AdminDao adminDao = new AdminDao();
                    Admin admin = adminDao.getAdmin(name);
                    adminDao.closeConnection();
                    if (admin == null) {
                        msg = "不存在该用户！";
                    }
                    if (admin != null) {
                        if (!password.equals(admin.getPassword())) {
                            msg = "密码错误";
                        } else {
                            if (admin.getStatus() == Admin.ADMIN_STATUS_DISABLE) {
                                msg = "该用户状态不可以，请联系管理员";
                            } else {
                                req.getSession().setAttribute("user", admin);
                                req.getSession().setAttribute("userType", type);
                            }
                        }
                    } else if (type == 2) {

                    } else if (type == 3) {

                    } else {

                        msg = "用户类型错误";
                    }

                }
            }catch(Exception e){
                    msg = "用户类型错误";
                }

            }

        resp.setCharacterEncoding(("utf-8"));

        resp.getWriter().write(msg);

    }
}


