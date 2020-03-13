package ischool.entity;

/**
 * 超级管理员实体
 * @GWHJN 跟我回江南
 */
public class Admin {
    public static int ADMIN_STATUS_ENABLE = 1;
    public static int ADMIN_STATUS_DISABLE = 0;
    private int id;
    private String name;
    private String password;
    private int status = ADMIN_STATUS_ENABLE;

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

