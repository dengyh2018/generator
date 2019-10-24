package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

public class ControlBean {
    private String targetPackage;

    @Deprecated
    private String targetProject;

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    @Override
    public String toString() {
        return "ServiceBean [targetPackage=" + targetPackage + ", targetProject=" + targetProject + "]";
    }

  
  

    
}
