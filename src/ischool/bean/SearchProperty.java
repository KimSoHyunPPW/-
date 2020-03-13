package ischool.bean;

/**
 * 封装查询字段
 * @GWHJN 跟我回江南
 */
public class SearchProperty {
    private String key;  //查询的字段名称
    private Object vaule; //查询的字段值
    private Operator operator;


    public SearchProperty(String key,Object vaule,Operator operator){
        this.key = key;
        this.vaule = vaule;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getVaule() {
        return vaule;
    }

    public void setVaule(Object vaule) {
        this.vaule = vaule;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }


}
