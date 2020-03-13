package ischool.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    private String dbUrl = "jdbc:mysql://localhost:3306/db_dormitory_manager?useUnicode=true&characterEncoding=utf8";
    private String dbUser = "root";
    private String dbPassword = "ppwppw0828";
    private String jdbcName = "com.mysql.jdbc.Driver";
    private Connection connection = null;
    public Connection getConnection(){
        try{
            Class.forName(jdbcName);
            connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            System.out.println(("数据库连接成功"));
        }catch (Exception e){
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        return  connection;
    }

    public static void main(String[] args){
        DbUtil dbUtil = new DbUtil();
        dbUtil.getConnection();
    }
}
