package ischool.servlet;


import ischool.bean.Page;
import ischool.dao.StudentDao;
import ischool.entity.Student;
import ischool.util.StringUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class StudentServlet extends HttpServlet {
    private static final long serialVersionUID = 4805778862607430850L;

    protected void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req,resp);
    }
    protected void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toStudentListView".equals(method)){
            req.getRequestDispatcher("view/studentList.jsp").forward(req,resp);

        }
        if("AddStudent".equals(method)){
            addStudent(req,resp);
        }
        if("StudentList".equals(method)){
            getStudentList(req,resp);
        }
    }

    private void getStudentList(HttpServletRequest req, HttpServletResponse resp) {
        int pageNumber = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("rows"));
        String name = req.getParameter("name");
        Student student = new Student();
        Map<String,Object> ret = new HashMap<String,Object>();
        student.setName(name);
        StudentDao studentDao = new StudentDao();
        Page page = new Page(pageNumber,pageSize);
        ret.put("rows",studentDao.findList(student,page));
        ret.put("total",studentDao.getTotal(student));
        studentDao.closeConnection();
        resp.setCharacterEncoding("utf-8");
        try {
            resp.getWriter().write(JSONObject.fromObject(ret).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String sex = req.getParameter("sex");
        String tel = req.getParameter("tel");
        String aclass = req.getParameter("aclass");
        resp.setCharacterEncoding("utf-8");
        if(StringUtil.isEmpty(name)){
            resp.getWriter().write("姓名不能为空");
            return;
        }
        if(StringUtil.isEmpty(password)){
            resp.getWriter().write("密码不能为空");
            return;
        }
        if(StringUtil.isEmpty(sex)){
            resp.getWriter().write("性别不能为空");
            return;
        }
        if(StringUtil.isEmpty(tel)){
            resp.getWriter().write("电话不能为空");
            return;
        }
        if(StringUtil.isEmpty(aclass)){
            resp.getWriter().write("班级不能为空");
            return;
        }
        Student student = new Student();
        student.setName(name);
        student.setPassword(password);
        student.setSex(sex);
        student.setTel(tel);
        student.setAclass(aclass);
        student.setSn(StringUtil.generateSn("S",""));
        StudentDao studentDao = new StudentDao();
        String msg = "添加失败";
        if(studentDao.add(student)){
            msg = "success";

        }
        studentDao.closeConnection();
        resp.getWriter().write(msg);
    }
}
