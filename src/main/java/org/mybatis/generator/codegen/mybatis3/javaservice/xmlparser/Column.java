package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

public class Column implements Comparable<Column>{
    /**
     * 列名称
     */
    private String name;
    
    /**
     * and 或 or 
     */
    /**
     * 条件，等于，大于等
     */
    private String paramType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    @Override
    public String toString() {
        return "ColumnBean [name=" + name + ", paramType=" + paramType + "]";
    }

    public int compareTo(Column oth) {
        return this.getName().compareTo(oth.getName());
    }
    
    

}
