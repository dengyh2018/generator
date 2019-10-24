package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

import java.util.Set;

public class ConfigBean {

    // 查询条件， 等于 + 等于 + 等于
    // 查询条件 ， 等于+在列表中
    // 查询条件， 等于+介于某个值
    private ServiceBean javaServiceGenerator;
    
    private ControlBean controlGenerator;

    private Set<TableBean> tables;

    public ServiceBean getJavaServiceGenerator() {
        return javaServiceGenerator;
    }

    public void setJavaServiceGenerator(ServiceBean javaServiceGenerator) {
        this.javaServiceGenerator = javaServiceGenerator;
    }

    public Set<TableBean> getTables() {
        return tables;
    }

    public void setTables(Set<TableBean> tables) {
        this.tables = tables;
    }

    public ControlBean getControlGenerator() {
        return controlGenerator;
    }

    public void setControlGenerator(ControlBean controlGenerator) {
        this.controlGenerator = controlGenerator;
    }

    @Override
    public String toString() {
        return "ConfigBean [javaServiceGenerator=" + javaServiceGenerator
                + ", controlGenerator=" + controlGenerator + ", tables=" + tables + "]";
    }

     

}
