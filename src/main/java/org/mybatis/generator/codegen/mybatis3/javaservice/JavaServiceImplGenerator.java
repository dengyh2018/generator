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

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.ClassRemarkEnum;
import org.mybatis.generator.codegen.mybatis3.javaservice.methods.MethodType;
import org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser.*;

import java.util.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author Jeff Butler
 */
public class JavaServiceImplGenerator extends AbstractJavaGenerator {

    public JavaServiceImplGenerator() {
        super();
    }

    private Interface getDaoInterface() {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        return interfaze;
    }

    ;

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.22", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();

        ConfigBean configBean = ConfigLoader.load(ShellRunner.shellRunerConfig);
        String packageA = configBean.getJavaServiceGenerator().getClassPackage();
        String packageB = configBean.getJavaServiceGenerator().getInterfacePackage();

        String typeNameA = "";
        FullyQualifiedJavaType extendType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
        typeNameA = packageA + "." + extendType.getShortName() + "ServiceImpl";

        FullyQualifiedJavaType baseService = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
        String typeNameB = packageB + "." + baseService.getShortName() + "Service";

        FullyQualifiedJavaType typeA = new FullyQualifiedJavaType(typeNameA);
        FullyQualifiedJavaType typeB = new FullyQualifiedJavaType(typeNameB);
        TopLevelClass topLevelClass = new TopLevelClass(typeA);

        topLevelClass.addSuperInterface(typeB);
        topLevelClass.addImportedType(typeB);

        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addAnnotation("@Service");
        FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType(annoType);

        commentGenerator.addJavaFileComment(topLevelClass);
        commentGenerator.addJavaClassComment(topLevelClass, introspectedTable, ClassRemarkEnum.Service基础类);

        addMethods(topLevelClass, configBean);
        addDaoJavaField(topLevelClass);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;
    }

    private void addDaoJavaField(TopLevelClass topLevelClass) {

        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
        Field field = new Field(lowerCaseFirstChar(fieldType.getShortName()), fieldType);
        field.setVisibility(JavaVisibility.PRIVATE);

        field.addAnnotation("@Resource");
        FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("javax.annotation.Resource");
        topLevelClass.addImportedType(annoType);

        topLevelClass.addField(field);

        topLevelClass.addImportedType(fieldType);
    }

    private FullyQualifiedJavaType getSuperClass() {
        FullyQualifiedJavaType superClass;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            superClass = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        } else {
            String rootClass = getRootClass();
            if (rootClass != null) {
                superClass = new FullyQualifiedJavaType(rootClass);
            } else {
                superClass = null;
            }
        }

        return superClass;
    }

    private boolean includePrimaryKeyColumns() {
        return !introspectedTable.getRules().generatePrimaryKeyClass() && introspectedTable.hasPrimaryKeyColumns();
    }

    private boolean includeBLOBColumns() {
        return !introspectedTable.getRules().generateRecordWithBLOBsClass() && introspectedTable.hasBLOBColumns();
    }

    /**
     * <生成方法>
     */
    private void addMethods(TopLevelClass topLevelClass, ConfigBean configBean) {
        // 数据库表名
        String table = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        // xml中<table>配置项
        if (configBean.getTables() != null && configBean.getTables().size() > 0) {
            Iterator<TableBean> tableIt = configBean.getTables().iterator();
            while (tableIt.hasNext()) {
                TableBean tableBean = tableIt.next();
                // Service生成文件配置项（单表）
                if (tableBean.getTableName().equals(table)) {
                    if (tableBean.getMethods() != null) {
                        // 配置生成的Service方法
                        Iterator<MethodBean> methodsIt = tableBean.getMethods().iterator();
                        while (methodsIt.hasNext()) {
                            MethodBean methodBean = methodsIt.next();
                            this.addServiceMethod(methodBean, topLevelClass);
                        }
                    }

                }
            }
        }
        // 生成根据主键查找 Service方法
        this.addSelectByPrimaryKeyMethod(topLevelClass);
        // 生成根据主键删除 Service方法
        this.addDeleteByPrimaryKeyMethod(topLevelClass);
        //dengyh
        //生成根据主键新增 Service方法
        this.addInsertByPrimaryKeySelectiveMethod(topLevelClass);
        // 生成根据主键更新 Service方法
        this.addUpdateByPrimaryKeySelectiveMethod(topLevelClass);
        // 生成根据唯一键查找 Service方法
        this.addSelectByUniqueKeyMethods(topLevelClass);
        // method.addBodyLine(sb.toString());
    }

    /**
     * <获得对应的列>
     */
    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns;
        if (includePrimaryKeyColumns()) {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getAllColumns();
            } else {
                introspectedColumns = introspectedTable.getNonBLOBColumns();
            }
        } else {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getNonPrimaryKeyColumns();
            } else {
                introspectedColumns = introspectedTable.getBaseColumns();
            }
        }

        return introspectedColumns;
    }

    public void addServiceMethod(MethodBean methodBean, TopLevelClass topLevelClass) {
        String methodType = methodBean.getMethodType();
        // Example类型
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        topLevelClass.addImportedType(exampleType);
        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
        String dao = lowerCaseFirstChar(fieldType.getShortName());
        // 生成查询方法
        if (isGenarateSelectMethod(methodType)) {
            Method method = new Method();
            method.addAnnotation("@Override");
            FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
            topLevelClass.addImportedType(annoType);
            method.addBodyLine(exampleType.getShortName() + " example = new " + exampleType.getShortName() + "();");

            BuildResult buildResult = methodBuild(methodBean, method, topLevelClass, "", MethodType.Query);
            method.setVisibility(JavaVisibility.PUBLIC);

            StringBuffer stringBuffer = new StringBuffer();

            FullyQualifiedJavaType returnType = null;
            if (!"1".equals(methodBean.getResultForQuery())) {
                stringBuffer.append("queryBy");
                returnType = FullyQualifiedJavaType.getNewListInstance();
                returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getExtendRecordType()));

                if (methodBean.getOrderBy() != null && !methodBean.getOrderBy().trim().equals("")) {
                    method.addBodyLine("example.setOrderByClause(\"" + methodBean.getOrderBy() + "\");");
                }

                method.addBodyLine("return " + dao + ".selectByExample( example );");
            } else {
                stringBuffer.append("selectBy");
                returnType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
                method.addBodyLine("List<" + returnType.getShortName() + "> list = " + dao + ".selectByExample( example );");
                method.addBodyLine("if(list != null && !list.isEmpty()){");
                method.addBodyLine("if(list.size()>1){");
                method.addBodyLine("throw new RuntimeException(\"获取记录异常，查询到多条结果\");");
                method.addBodyLine("}else{");
                method.addBodyLine("return list.get(0);");
                method.addBodyLine("}");
                method.addBodyLine("}");
                method.addBodyLine("return null;");
            }
            FullyQualifiedJavaType listType = new FullyQualifiedJavaType("java.util.List");
            topLevelClass.addImportedType(listType);

            stringBuffer.append(buildResult.getMethodSign());

            // 未设置制定方法名
            if (com.mysql.jdbc.StringUtils.isNullOrEmpty(methodBean.getMethodName())) {
                method.setName(stringBuffer.toString());
            }
            // 有设置制定的方法名
            else {
                method.setName(methodBean.getMethodName());
            }

            topLevelClass.addImportedType(returnType);
            method.setReturnType(returnType);

            stringBuffer.setLength(0);
            topLevelClass.addMethod(method);
        }

        // 生成更新方法
        if (isGenarateUpdateMethod(methodType)) {
            Method method = new Method();
            method.addAnnotation("@Override");
            FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
            topLevelClass.addImportedType(annoType);
            FullyQualifiedJavaType beanParamType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
            String beanParamName = lowerCaseFirstChar(beanParamType.getShortName());

            method.addBodyLine(exampleType.getShortName() + " example = new " + exampleType.getShortName() + "();");

            BuildResult buildResult = methodBuild(methodBean, method, topLevelClass, "", MethodType.Update);
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addBodyLine("return " + dao + ".updateByExample( " + beanParamName + " , example );");
            // 使用配置文件定义名称
            if (null != methodBean.getMethodName()) {
                method.setName(methodBean.getMethodName());
            }
            // 使用默认的生成方法
            else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("updateBy");
                stringBuffer.append(buildResult.getMethodSign());
                method.setName(stringBuffer.toString());
                stringBuffer.setLength(0);
            }

            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.int");
            method.setReturnType(returnType);
            topLevelClass.addMethod(method);
        }

        // 生成删除方法
        if (isGenarateDeleteMethod(methodType)) {
            Method method = new Method();
            method.addAnnotation("@Override");
            FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
            topLevelClass.addImportedType(annoType);
            method.addBodyLine(exampleType.getShortName() + " example = new " + exampleType.getShortName() + "();");

            BuildResult buildResult = methodBuild(methodBean, method, topLevelClass, "", MethodType.Delete);
            method.setVisibility(JavaVisibility.PUBLIC);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("deleteBy");
            stringBuffer.append(buildResult.getMethodSign());
            method.addBodyLine("return " + dao + ".deleteByExample( example );");

            method.setName(stringBuffer.toString());
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.int");
            method.setReturnType(returnType);
            stringBuffer.setLength(0);
            topLevelClass.addMethod(method);
        }

    }

    private BuildResult methodBuild(MethodBean methodBean, Method method, TopLevelClass topLevelClass, String paramPrx, MethodType methodType) {
        BuildResult buildResult = new BuildResult();
        FullyQualifiedJavaType beanParamType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
        String beanParamName = lowerCaseFirstChar(beanParamType.getShortName());
        if (methodType.equals(MethodType.Update)) {
            method.addParameter(new Parameter(beanParamType, beanParamName));
            topLevelClass.addImportedType(beanParamType);
        }
        StringBuffer str = new StringBuffer();
        StringBuffer methodLineSb = new StringBuffer();
        int paramCountA = 1;
        methodLineSb.append("example.createCriteria()");

        int count = 0;
        int count2 = 0;
        if (methodBean.getAnd() != null) {
            count += 1;
            count2 = count;
        }
        if (methodBean.getOrs() != null && !methodBean.getOrs().isEmpty()) {
            count += methodBean.getOrs().size();
        }

        for (int ci = 0; ci < count; ci++) {
            List<Column> columnBeanList = null;
            if (ci < count2) {
                if (methodBean.getAnd() != null) {
                    columnBeanList = methodBean.getAnd().getColumns();
                }
            } else {
                if (methodBean.getOrs() != null && !methodBean.getOrs().isEmpty()) {
                    methodLineSb.append(";");
                    methodLineSb.append('\r');
                    for (int t = 0; t < 8; t++) {
                        methodLineSb.append(" ");
                    }
                    methodLineSb.append("example.or()");
                    columnBeanList = methodBean.getOrs().get(ci - count2).getColumns();
                    str.append("Or");
                    paramPrx = "o";
                } else {
                    continue;
                }
            }

            if (columnBeanList == null || columnBeanList.isEmpty()) {
                continue;
            }

            Iterator<Column> columnsIt = columnBeanList.iterator();
            List<IntrospectedColumn> dbColumnList = getColumnsInThisClass();

            int paramCount = 1;

            while (columnsIt.hasNext()) {
                Column columnBean = columnsIt.next();
                Iterator<IntrospectedColumn> dbColumnIt = dbColumnList.iterator();
                boolean hasFoundColumn = false;
                while (dbColumnIt.hasNext()) {
                    IntrospectedColumn introspectedColumn = dbColumnIt.next();

                    if (paramCountA == 1) {
                        System.out.println(introspectedColumn.getActualColumnName());
                    }

                    if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnBean.getName())) {
                        String javaProperty = introspectedColumn.getJavaProperty();
                        String javaFieldName = upperCaseFirstChar(introspectedColumn.getJavaProperty());
                        ParamConditionEnum em = ParamConditionEnum.getByEnumName(columnBean.getParamType());
                        // 获取枚举名称
                        String coditionName = em.name();

                        if (methodLineSb.toString().indexOf(".and") > 0 && !methodLineSb.toString().endsWith(".or()")) {
                            methodLineSb.append('\r');
                            for (int t = 0; t < 32; t++) {
                                methodLineSb.append(" ");
                            }
                        }

                        methodLineSb.append(".and" + javaFieldName + coditionName);
                        methodLineSb.append("(");

                        str.append(javaFieldName).append(coditionName);
                        if (columnsIt.hasNext()) {
                            str.append("And");
                        }
                        for (int i = 0; i < em.getInputCount(); i++) {
                            if (coditionName.contains("In")) {
                                FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
                                paramType.addTypeArgument(introspectedColumn.getFullyQualifiedJavaType());
                                String paramName = javaProperty + "s" + paramPrx;
                                methodLineSb.append(paramName);
                                method.addParameter(new Parameter(paramType, paramName)); // $NON-NLS-1$
                                topLevelClass.addImportedType(paramType);
                            } else {
                                String paramName = javaProperty + paramPrx;
                                methodLineSb.append(paramName);
                                method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), paramName)); // $NON-NLS-1$
                                topLevelClass.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
                            }
                            if (i != em.getInputCount() - 1) {
                                methodLineSb.append(",");
                            }
                            paramCount++;
                        }
                        methodLineSb.append(")");
                        hasFoundColumn = true;
                    }
                }
                if (!hasFoundColumn) {
                    throw new RuntimeException("找不到列" + columnBean.getName() + "请检查service配置文件");
                }
                paramCountA = 2;
            }

        }

        methodLineSb.append(";");
        methodLineSb.append('\r');

        for (int t = 0; t < 5; t++) {
            methodLineSb.append(" ");
        }
        method.addBodyLine(methodLineSb.toString());
        buildResult.setMethodSign(str.toString());
        buildResult.setMethodLine(methodLineSb.toString());
        return buildResult;
    }

    private void addDeleteByPrimaryKeyMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            Method method = new Method();
            Set<FullyQualifiedJavaType> importTypes = new HashSet<FullyQualifiedJavaType>();
            method.addAnnotation("@Override");
            FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
            importTypes.add(annoType);
            method.setVisibility(JavaVisibility.PUBLIC);

            FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
            importTypes.add(fieldType);

            String dao = lowerCaseFirstChar(fieldType.getShortName());

            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();

            StringBuilder sb = new StringBuilder();
            int j = 0;
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                j++;
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                if (j < introspectedColumns.size()) {
                    sb.append(introspectedColumn.getJavaProperty() + ", ");
                } else {
                    sb.append(introspectedColumn.getJavaProperty());
                }

                method.addParameter(parameter);
            }

            method.addBodyLine(
                    "return " + dao + "." + introspectedTable.getDeleteByPrimaryKeyStatementId() + "(" + sb.toString() + ") > 0;");
            method.setName(introspectedTable.getDeleteByPrimaryKeyStatementId());
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.boolean");
            method.setReturnType(returnType);

            if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, getDaoInterface(), introspectedTable)) {
                topLevelClass.addImportedTypes(importTypes);
                topLevelClass.addMethod(method);
            }
        }

    }

    private void addSelectByPrimaryKeyMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            Method method = new Method();
            Set<FullyQualifiedJavaType> importTypes = new HashSet<FullyQualifiedJavaType>();

            FullyQualifiedJavaType recordType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
            importTypes.add(recordType);

            method.addAnnotation("@Override");
            FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
            importTypes.add(annoType);
            method.setVisibility(JavaVisibility.PUBLIC);

            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();

            StringBuilder sb = new StringBuilder();
            int j = 0;
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                j++;
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importTypes.add(type);

                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                if (j < introspectedColumns.size()) {
                    sb.append(introspectedColumn.getJavaProperty() + ", ");
                } else {
                    sb.append(introspectedColumn.getJavaProperty());
                }

                method.addParameter(parameter);
            }

            FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
            importTypes.add(fieldType);
            String dao = lowerCaseFirstChar(fieldType.getShortName());

            method.addBodyLine("return " + dao + "." + introspectedTable.getSelectByPrimaryKeyStatementId() + "(" + sb.toString() + ");");
            method.setName(introspectedTable.getSelectByPrimaryKeyStatementId());

            method.setReturnType(recordType);

            if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, getDaoInterface(), introspectedTable)) {
                topLevelClass.addImportedTypes(importTypes);
                topLevelClass.addMethod(method);
            }
        }
    }

    private void addUpdateByPrimaryKeySelectiveMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            Method method = new Method();
            Set<FullyQualifiedJavaType> importTypes = new HashSet<FullyQualifiedJavaType>();

            FullyQualifiedJavaType recoreType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
            importTypes.add(recoreType);

            String paramName = lowerCaseFirstChar(recoreType.getShortName());
            method.addAnnotation("@Override");
            FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
            importTypes.add(annoType);

            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(new Parameter(recoreType, paramName));

            FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
            importTypes.add(fieldType);

            String dao = lowerCaseFirstChar(fieldType.getShortName());

            method.addBodyLine(
                    "return " + dao + "." + introspectedTable.getUpdateByPrimaryKeySelectiveStatementId() + "(" + paramName + ") > 0;");
            method.setName(introspectedTable.getUpdateByPrimaryKeySelectiveStatementId());
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.boolean");
            method.setReturnType(returnType);

            if (context.getPlugins().clientUpdateByPrimaryKeySelectiveMethodGenerated(method, getDaoInterface(), introspectedTable)) {
                topLevelClass.addImportedTypes(importTypes);
                topLevelClass.addMethod(method);
            }
        }

    }

    private void addInsertByPrimaryKeySelectiveMethod(TopLevelClass topLevelClass) {
        //if (introspectedTable.getRules().generateInsertSelective()) {
        Method method = new Method();
        Set<FullyQualifiedJavaType> importTypes = new HashSet<FullyQualifiedJavaType>();

        FullyQualifiedJavaType recoreType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
        importTypes.add(recoreType);

        String paramName = lowerCaseFirstChar(recoreType.getShortName());
        method.addAnnotation("@Override");
        FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
        importTypes.add(annoType);

        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(recoreType, paramName));

        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
        importTypes.add(fieldType);

        String dao = lowerCaseFirstChar(fieldType.getShortName());

        method.addBodyLine(
                "return " + dao + "." + introspectedTable.getInsertSelectiveStatementId() + "(" + paramName + ") > 0;");
        method.setName(introspectedTable.getInsertSelectiveStatementId());
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.boolean");
        method.setReturnType(returnType);

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(method, getDaoInterface(), introspectedTable)) {
            topLevelClass.addImportedTypes(importTypes);
            topLevelClass.addMethod(method);
        }
        //}

    }

    private void addSelectByUniqueKeyMethods(TopLevelClass topLevelClass) {

        Map<String, List<IntrospectedColumn>> uniqueMap = introspectedTable.getUniqueMap();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        for (List<IntrospectedColumn> cols : uniqueMap.values()) {
            Method method = new Method();
            method.addAnnotation("@Override");
            FullyQualifiedJavaType annoType = new FullyQualifiedJavaType("java.lang.Override");
            topLevelClass.addImportedType(annoType);

            method.setVisibility(JavaVisibility.PUBLIC);
            FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();
            method.setReturnType(returnType);
            importedTypes.add(returnType);
            String methodName = introspectedTable.getSelectByUniqueStatementId() + introspectedTable.getMethodPartName(cols);
            method.setName(methodName);

            StringBuffer sb = new StringBuffer();
            FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ExtendJavaMapperType());
            String dao = lowerCaseFirstChar(fieldType.getShortName());

            int j = 0;
            sb.append(dao).append(".").append(methodName).append("( ");
            for (IntrospectedColumn introspectedColumn : cols) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                method.addParameter(parameter);

                if (j == cols.size() - 1) {
                    sb.append(introspectedColumn.getJavaProperty()).append(" );");
                } else {
                    sb.append(introspectedColumn.getJavaProperty()).append(", ");
                }
                j++;
            }
            method.addBodyLine("return " + sb.toString());
            context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }

    }

    private static boolean isGenarateSelectMethod(String methodType) {
        if (methodType != null && methodType.toLowerCase().contains("q")) {
            return true;
        }
        return false;
    }

    private static boolean isGenarateUpdateMethod(String methodType) {
        if (methodType != null && methodType.toLowerCase().contains("u")) {
            return true;
        }
        return false;
    }

    private static boolean isGenarateDeleteMethod(String methodType) {
        if (methodType != null && methodType.toLowerCase().contains("d")) {
            return true;
        }
        return false;
    }

}
