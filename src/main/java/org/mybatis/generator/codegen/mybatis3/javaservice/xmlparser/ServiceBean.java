package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

public class ServiceBean {
	private String interfacePackage;

	private String classPackage;

	private String targetProject;

	public String getInterfacePackage() {
		return interfacePackage;
	}

	public void setInterfacePackage(String interfacePackage) {
		this.interfacePackage = interfacePackage;
	}

	public String getClassPackage() {
		return classPackage;
	}

	public void setClassPackage(String classPackage) {
		this.classPackage = classPackage;
	}

	@Override
	public String toString() {
		return "ServiceBean [interfacePackage=" + interfacePackage + ", classPackage=" + classPackage + "]";
	}

	public String getTargetProject() {
		return targetProject;
	}

	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}

}
