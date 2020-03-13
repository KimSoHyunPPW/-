package ischool.dao;

import ischool.bean.Page;
import ischool.bean.SearchProperty;
import ischool.util.DbUtil;
import ischool.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库操作基础类
 * 利用泛型和反射机制来抽象数据库基本的增删改查操作
 * @GWHJN 跟我回江南
 */
public class BaseDao<T> {

    public  final int CURD_ADD =1;

    public  final int CURD_DELETE =2;

    public  final int CURD_UPDATE =3;

    public  final int CURD_SELECT =4;

    public  final int CURD_COUNT =5;


    public Connection con = new DbUtil().getConnection();

    private Class<T> t;

    /**
     * 构造函数中明确传进来的参数对象
     *
     *
     */

    public BaseDao(){
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if(actualTypeArguments.length > 0){
                t = (Class<T>) actualTypeArguments[0];
            }
        }
    }


    /**
     *
     * 所有新增插入操作抽象封装
     * @param t
     * @return
     * @throws SQLException
     */

    public boolean add(T t) throws SQLException {
        if(t== null) return false;
        String buildSql = buildSql(CURD_ADD);

        try {
            PreparedStatement preparedStatement = con.prepareStatement(buildSql);
            Field[] fields = t.getClass().getDeclaredFields();
            for(int i=1;i<fields.length;i++ ){
                fields[i].setAccessible(true);
                preparedStatement.setObject(i,fields[i].get(t));
            }
            return preparedStatement.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 抽象封装所有的分页查询列表操作
     * @param page
     * @return
     */
    public Page<T> findList(Page<T> page){
        String sql = buildSql(CURD_SELECT);
        sql += buildSearchSql(page);
        sql += " limit " + page.getOffset() + "," + page.getPageSize();
        try{
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement = setParams(page,preparedStatement);
            ResultSet executeQuery = preparedStatement.executeQuery();
            List<T> conten = page.getConten();
            while (executeQuery.next()){
                T entity = this.t.newInstance();
                Field[] declaredFields = t.getDeclaredFields();
                for(Field filed :declaredFields){
                    filed.setAccessible(true);
                    filed.set(entity,executeQuery.getObject(StringUtil.converToUnderLine(filed.getName())));
                }
                conten.add(entity);

            }
            page.setConten(conten);

        }catch (Exception e){

        }
        page.setTotal(getTotal(page));

        return page;
    }


    /**
     * 获取符合条件的所有记录数
     * @param page
     * @return
     */
    public int getTotal(Page<T> page){
        String sql = buildSql(CURD_COUNT);
        sql += buildSearchSql(page);

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement = setParams(page,preparedStatement);
            ResultSet executeQuery = preparedStatement.executeQuery();
            if(executeQuery.next()){
                return executeQuery.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }


    /**
     *
     * 获取所有类型的字段名称
     * @param t
     * @return
     */
    private List<String> getFields(T t){
        List<String> fieldsList = new ArrayList<String>();
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for(Field field : declaredFields){
            fieldsList.add(field.getName());
        }
        return fieldsList;
    }


    /**
     *给构造的查询连接赋值参数
     * @param page
     * @param preparedStatement
     * @return
     */
    private PreparedStatement setParams(Page<T> page,PreparedStatement preparedStatement){
        List<SearchProperty> searchProperties = page.getSearchProperties();
        int index = 1;
        for(SearchProperty searchProperty : searchProperties){
            try {
                preparedStatement.setObject(index++,searchProperty.getVaule());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return preparedStatement;
    }


    /**
     *
     * 构造查询sql语句
     * @param page
     * @return
     */
    private String buildSearchSql(Page<T> page){
        String sql = "";
        List<SearchProperty> searchProperties = page.getSearchProperties();
        for(SearchProperty searchProperty : searchProperties){
            switch(searchProperty.getOperator()){
                case GT:{
                    sql += " and " + StringUtil.converToUnderLine(searchProperty.getKey()) + " > ?";
                    break;
                }
                case GTE: {
                    sql += " and " + StringUtil.converToUnderLine(searchProperty.getKey()) + " >= ?";
                    break;
                }
                case EQ: {
                    sql += " and " + StringUtil.converToUnderLine(searchProperty.getKey()) + " = ?";
                    break;
                }
                case LT: {
                    sql += " and " + StringUtil.converToUnderLine(searchProperty.getKey()) + " < ?";
                    break;
                }
                case LTE: {
                    sql += " and " + StringUtil.converToUnderLine(searchProperty.getKey()) + " <= ?";
                    break;
                }
                case LIKE: {
                    sql += " and " + StringUtil.converToUnderLine(searchProperty.getKey()) + " like ?";
                    break;
                }
                case NEQ: {
                    sql += " and " + StringUtil.converToUnderLine(searchProperty.getKey()) + " <> ?";
                    break;
                }
            }
        }
        sql = sql.replaceFirst("and","where");
        return sql;
    }


    /**
     *
     * 构造一般查询语句
     * @param type
     * @return
     */
    private String buildSql(int type) {
        String sql = "";
        switch (type){
            case CURD_ADD:{
                String sql1 = "insert into " + StringUtil.converToUnderLine(t.getSimpleName()) + "(";
                Field[] declaredFields = t.getDeclaredFields();
                for(Field field : declaredFields){
                    sql1 += StringUtil.converToUnderLine(field.getName()) + ",";

                }
                sql1 = sql1.substring(0,sql1.length()-1)+")";
                String sql2 = "values(null,";
                String[] params = new String[declaredFields.length - 1];
                Arrays.fill(params,"?");
                sql2 += String.join(",", params) + ")";
                sql = sql1 + sql2;
                break;
            }
            case CURD_SELECT:{
                sql = "sleect * from " + t.getSimpleName();
                break;
            }
            case CURD_COUNT:{
                sql = "select count(*) as total from " + t.getSimpleName();
                break;
            }
            default:
                break;
        }



        return sql;
    }


    /**
     * 关闭数据库连接
     */
    public void closeConnection(){
        if(con != null){
            try {
                con.close();
                System.out.println(t.getSimpleName()+"Dao建立的数据库连接成功关闭");
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

}
