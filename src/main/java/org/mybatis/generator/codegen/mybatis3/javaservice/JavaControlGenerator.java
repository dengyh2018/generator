/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javaservice;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser.ConfigBean;
import org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser.MethodBean;
import org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser.TableBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class JavaControlGenerator extends AbstractJavaGenerator {

	public JavaControlGenerator() {
		super();
	}

	@Override
	public List<CompilationUnit> getCompilationUnits() {
//		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
//		progressCallback.startTask(getString("Progress.23", table.toString())); //$NON-NLS-1$
//		CommentGenerator commentGenerator = context.getCommentGenerator();
//
//		ConfigBean configBean = ConfigLoader.load(ShellRunner.shellRunerConfig);
//		String packageA = configBean.getControlGenerator().getTargetPackage();
//
//		String typeNameA = "";
//		FullyQualifiedJavaType extendType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
//		typeNameA = packageA + "." + extendType.getShortName() + "Controller"; //Control
//
//		FullyQualifiedJavaType typeA = new FullyQualifiedJavaType(typeNameA);
//		TopLevelClass topLevelClass = new TopLevelClass(typeA);
//
//		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
//		topLevelClass.addAnnotation("@Controller");
//		FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("org.springframework.stereotype.Controller");
//		topLevelClass.addImportedType(annoType);
//
//		commentGenerator.addJavaFileComment(topLevelClass);
//
//		addSuperClass(topLevelClass);
//		addMethods(topLevelClass, configBean);
//		addServiceJavaField(topLevelClass, configBean);

		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
//		answer.add(topLevelClass);
		return answer;
	}

	private void addServiceJavaField(TopLevelClass topLevelClass, ConfigBean configBean) {

		String typeName = configBean.getJavaServiceGenerator().getInterfacePackage();

		//getExtendRecordType
		FullyQualifiedJavaType baseService = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
		typeName += "." + baseService.getShortName() + "Service";

		FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(typeName);
		;

		Field field = new Field(lowerCaseFirstChar(fieldType.getShortName()), fieldType);
		field.setVisibility(JavaVisibility.PRIVATE);

		field.addAnnotation("@Resource");
		FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("javax.annotation.Resource");
		topLevelClass.addImportedType(annoType);

		topLevelClass.addField(field);

		topLevelClass.addImportedType(fieldType);
	}

	private void addSuperClass(TopLevelClass topLevelClass) {
		FullyQualifiedJavaType superClass = new FullyQualifiedJavaType("com.touna.lhh.control.base.BaseControl");
		// topLevelClass.addSuperInterface(superClass);
	}

	/**
	 * 
	 * <生成方法>
	 * 
	 */
	private void addMethods(TopLevelClass topLevelClass, ConfigBean configBean) {
		String table = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
		Iterator<TableBean> tableIt = configBean.getTables().iterator();
		while (tableIt.hasNext()) {
			TableBean tableBean = tableIt.next();
			if (tableBean.getTableName().equals(table)) {
				Iterator<MethodBean> methodsIt = tableBean.getMethods().iterator();
				while (methodsIt.hasNext()) {
					MethodBean methodBean = methodsIt.next();
					// this.addServiceMethod(methodBean, topLevelClass);
				}
			}
		}

		// method.addBodyLine(sb.toString());
	}

	// public void addServiceMethod(MethodBean methodBean, TopLevelClass topLevelClass) {
	// int methodType = methodBean.getMethodType();
	// // Example类型
	// FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
	// topLevelClass.addImportedType(exampleType);
	// FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
	// String dao = lowerCaseFirstChar(fieldType.getShortName());
	// // 生成查询方法
	// if (isGenarateSelectMethod(methodType)) {
	// Method method = new Method();
	// method.addAnnotation("@Override");
	// FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
	// topLevelClass.addImportedType(annoType);
	// method.addBodyLine(exampleType.getShortName() + " example = new " + exampleType.getShortName() + "();");
	//
	// BuildResult buildResult = methodBuild(methodBean, method, topLevelClass, "", MethodType.Query);
	// method.setVisibility(JavaVisibility.PUBLIC);
	//
	// StringBuffer stringBuffer = new StringBuffer();
	//
	//
	// FullyQualifiedJavaType returnType = null;
	// if (!"1".equals(methodBean.getResultForQuery())) {
	// stringBuffer.append("queryBy");
	// returnType = FullyQualifiedJavaType.getNewListInstance();
	// returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getExtendRecordType()));
	//
	// if(methodBean.getOrderBy()!=null&&!methodBean.getOrderBy().trim().equals("")){
	// method.addBodyLine("example.setOrderByClause(\""+methodBean.getOrderBy()+"\");");
	// }
	//
	//
	// method.addBodyLine("return " + dao + ".selectByExample( example );");
	// }
	// else {
	// stringBuffer.append("selectBy");
	// returnType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
	// method.addBodyLine("List<"+returnType.getShortName()+"> list = " + dao + ".selectByExample( example );");
	// method.addBodyLine("if(list != null && !list.isEmpty()){");
	// method.addBodyLine("if(list.size()>1){");
	// method.addBodyLine("throw new RuntimeException(\"获取记录异常，查询到多条结果\");");
	// method.addBodyLine("}else{");
	// method.addBodyLine("return list.get(0);");
	// method.addBodyLine("}");
	// method.addBodyLine("}");
	// method.addBodyLine("return null;");
	// }
	//
	// stringBuffer.append(buildResult.getMethodSign());
	//
	// method.setName(stringBuffer.toString());
	// topLevelClass.addImportedType(returnType);
	// method.setReturnType(returnType);
	//
	// stringBuffer.setLength(0);
	// topLevelClass.addMethod(method);
	// }
	//
	//
	//
	// }

}
