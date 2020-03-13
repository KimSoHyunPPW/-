package ischool.dao;

import ischool.bean.Page;
import ischool.entity.Student;
import ischool.util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @GWHJN 跟我回江南
 * 学生实体数据库操作
 */
public class StudentDao extends BaseDao<Student>{
    public boolean add(Student student){
        String sql = "insert into student(id,sn,name,password,sex,tel,aclass) values(null,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,student.getSn());
            preparedStatement.setString(2,student.getName());
            preparedStatement.setString(3,student.getPassword());
            preparedStatement.setString(4,student.getSex());
            preparedStatement.setString(5,student.getTel());
            preparedStatement.setString(6,student.getAclass());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    public List<Student> findList(Student student, Page page){
        List<Student> ret = new ArrayList<Student>();
        String sql = "select * from student ";
        if(!StringUtil.isEmpty(student.getName())){
            sql += "where name like '%" + student.getName() + "%' ";
        }
        sql += "limit " + page.getOffset() + "," + page.getPageSize();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet executeQuery = preparedStatement.executeQuery();
            while(executeQuery.next()){
                Student s = new Student();
                s.setId(executeQuery.getInt("id"));
                s.setName(executeQuery.getString("name"));
                s.setPassword(executeQuery.getString("password"));
                s.setSex(executeQuery.getString("sex"));
                s.setSn(executeQuery.getString("sn"));
                s.setTel(executeQuery.getString("tel"));
                s.setAclass(executeQuery.getString("aclass"));
                ret.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public int getTotal(Student student){
        String sql = "select count(id) as total from student ";
        if(!StringUtil.isEmpty(student.getName())){
            sql += "where name like '%" + student.getName() + "%' ";
        }
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet executeQuery = preparedStatement.executeQuery();
            if(executeQuery.next()){
                return executeQuery.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
