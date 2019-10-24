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
public class JavaServiceGenerator extends AbstractJavaGenerator {

    public JavaServiceGenerator() {
        super();
    }

    private Interface getDaoInterface() {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        return interfaze;
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.21", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();

        ConfigBean configBean = ConfigLoader.load(ShellRunner.shellRunerConfig);
        String typeName = configBean.getJavaServiceGenerator().getInterfacePackage();

        FullyQualifiedJavaType baseService = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
        typeName += "." + baseService.getShortName() + "Service";

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(typeName);

        Interface topLevelInterface = new Interface(type);

        topLevelInterface.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelInterface);

        commentGenerator.addJavaClassComment(topLevelInterface, introspectedTable, ClassRemarkEnum.Service基础接口);

        addMethods(topLevelInterface, configBean);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelInterface);
        return answer;
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
    private void addMethods(Interface topLevelInterface, ConfigBean configBean) {
        String table = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();

        if (configBean.getTables() != null && configBean.getTables().size() > 0) {
            Iterator<TableBean> tableIt = configBean.getTables().iterator();
            while (tableIt.hasNext()) {
                TableBean tableBean = tableIt.next();
                if (tableBean.getTableName().equals(table)) {
                    if (tableBean.getMethods() != null) {
                        Iterator<MethodBean> methodsIt = tableBean.getMethods().iterator();
                        while (methodsIt.hasNext()) {
                            MethodBean methodBean = methodsIt.next();
                            this.addServiceMethod(methodBean, topLevelInterface);
                        }
                    }
                }
            }
        }

        this.addSelectByPrimaryKeyMethod(topLevelInterface);
        this.addDeleteByPrimaryKeyMethod(topLevelInterface);
        //dengyh
        this.addInsertByPrimaryKeySelectiveMethod(topLevelInterface);
        this.addUpdateByPrimaryKeySelectiveMethod(topLevelInterface);
        this.addSelectByUniqueKeyMethods(topLevelInterface);
        // method.addBodyLine(sb.toString());
    }

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

    public void addServiceMethod(MethodBean methodBean, Interface topLevelInterface) {
        String methodType = methodBean.getMethodType();
        // 生成查询方法
        if (isGenarateSelectMethod(methodType)) {
            Method method = new Method();
            BuildResult buildResult = methodBuild(methodBean, method, topLevelInterface, "", MethodType.Query);
            method.setVisibility(JavaVisibility.PUBLIC);
            StringBuffer stringBuffer = new StringBuffer();

            FullyQualifiedJavaType returnType = null;
            if (!"1".equals(methodBean.getResultForQuery())) {
                stringBuffer.append("queryBy");
                returnType = FullyQualifiedJavaType.getNewListInstance();
                returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getExtendRecordType()));
            } else {
                stringBuffer.append("selectBy");
                returnType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
            }
            stringBuffer.append(buildResult.getMethodSign());

            // 未设置制定方法名
            if (com.mysql.jdbc.StringUtils.isNullOrEmpty(methodBean.getMethodName())) {
                method.setName(stringBuffer.toString());
            }
            // 有设置制定的方法名
            else {
                method.setName(methodBean.getMethodName());
            }

            topLevelInterface.addImportedType(returnType);
            method.setReturnType(returnType);
            stringBuffer.setLength(0);
            topLevelInterface.addMethod(method);
        }

        // 生成更新方法
        if (isGenarateUpdateMethod(methodType)) {
            Method method = new Method();

            method.setVisibility(JavaVisibility.PUBLIC);

            BuildResult buildResult = methodBuild(methodBean, method, topLevelInterface, "", MethodType.Update);
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
            topLevelInterface.addMethod(method);
        }

        // 生成删除方法
        if (isGenarateDeleteMethod(methodType)) {
            Method method = new Method();
            BuildResult buildResult = methodBuild(methodBean, method, topLevelInterface, "", MethodType.Delete);
            method.setVisibility(JavaVisibility.PUBLIC);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("deleteBy");
            stringBuffer.append(buildResult.getMethodSign());

            method.setName(stringBuffer.toString());
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.int");
            method.setReturnType(returnType);
            stringBuffer.setLength(0);
            topLevelInterface.addMethod(method);
        }

    }

    private BuildResult methodBuild(MethodBean methodBean, Method method, Interface topLevelInterface, String paramPrx, MethodType methodType) {
        BuildResult buildResult = new BuildResult();
        FullyQualifiedJavaType beanParamType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
        String beanParamName = lowerCaseFirstChar(beanParamType.getShortName());
        if (methodType.equals(MethodType.Update)) {
            method.addParameter(new Parameter(beanParamType, beanParamName));
            topLevelInterface.addImportedType(beanParamType);
        }

        StringBuffer str = new StringBuffer();
        int paramCountA = 1;

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

                        str.append(javaFieldName).append(coditionName);
                        // and逻辑使用and关键字连接
                        if (columnsIt.hasNext()) {
                            str.append("And");
                        }
                        for (int i = 0; i < em.getInputCount(); i++) {
                            if (coditionName.contains("In")) {
                                FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
                                paramType.addTypeArgument(introspectedColumn.getFullyQualifiedJavaType());
                                String paramName = javaProperty + "s" + paramPrx;
                                method.addParameter(new Parameter(paramType, paramName)); // $NON-NLS-1$
                                topLevelInterface.addImportedType(paramType);
                            } else {
                                String paramName = javaProperty + paramPrx;
                                method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), paramName)); // $NON-NLS-1$
                                topLevelInterface.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
                            }
                            if (i != em.getInputCount() - 1) {
                            }
                            paramCount++;
                        }
                        hasFoundColumn = true;
                    }
                }
                if (!hasFoundColumn) {
                    throw new RuntimeException("找不到列" + columnBean.getName() + "请检查service配置文件");
                }
                paramCountA = 2;
            }

        }
        buildResult.setMethodSign(str.toString());
        return buildResult;
    }

    private void addDeleteByPrimaryKeyMethod(Interface topLevelInterface) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);

            method.setName(introspectedTable.getDeleteByPrimaryKeyStatementId());

            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();

            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                topLevelInterface.addImportedType(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());

                method.addParameter(parameter);
            }

            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.boolean");
            method.setReturnType(returnType);
            if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, getDaoInterface(), introspectedTable)) {
                topLevelInterface.addMethod(method);
            }
        }

    }

    private void addSelectByPrimaryKeyMethod(Interface topLevelInterface) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            Method method = new Method();
            Set<FullyQualifiedJavaType> importTypes = new HashSet<FullyQualifiedJavaType>();
            FullyQualifiedJavaType recordType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
            importTypes.add(recordType);

            method.setVisibility(JavaVisibility.PUBLIC);

            method.setName(introspectedTable.getSelectByPrimaryKeyStatementId());

            method.setReturnType(recordType);

            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();

            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());

                method.addParameter(parameter);
            }

            if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, getDaoInterface(), introspectedTable)) {
                topLevelInterface.addImportedTypes(importTypes);
                topLevelInterface.addMethod(method);
            }
        }
    }

    private void addUpdateByPrimaryKeySelectiveMethod(Interface topLevelInterface) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            Method method = new Method();
            FullyQualifiedJavaType recoreType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
            topLevelInterface.addImportedType(recoreType);

            String paramName = lowerCaseFirstChar(recoreType.getShortName());
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(new Parameter(recoreType, paramName));

            method.setName(introspectedTable.getUpdateByPrimaryKeySelectiveStatementId());
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.boolean");
            method.setReturnType(returnType);

            if (context.getPlugins().clientUpdateByPrimaryKeySelectiveMethodGenerated(method, getDaoInterface(), introspectedTable)) {
                topLevelInterface.addMethod(method);
            }
        }
    }

    private void addInsertByPrimaryKeySelectiveMethod(Interface topLevelInterface) {
        //if (introspectedTable.getRules().generateInsertSelective()) {
        Method method = new Method();
        FullyQualifiedJavaType recoreType = new FullyQualifiedJavaType(introspectedTable.getExtendRecordType());
        topLevelInterface.addImportedType(recoreType);

        String paramName = lowerCaseFirstChar(recoreType.getShortName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(recoreType, paramName));

        method.setName(introspectedTable.getInsertSelectiveStatementId());
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.boolean");
        method.setReturnType(returnType);

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(method, getDaoInterface(), introspectedTable)) {
            topLevelInterface.addMethod(method);
        }
        //}
    }

    private void addSelectByUniqueKeyMethods(Interface topLevelInterface) {

        Map<String, List<IntrospectedColumn>> uniqueMap = introspectedTable.getUniqueMap();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        for (List<IntrospectedColumn> cols : uniqueMap.values()) {
            Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();
            method.setReturnType(returnType);
            importedTypes.add(returnType);
            method.setName(introspectedTable.getSelectByUniqueStatementId() + introspectedTable.getMethodPartName(cols));

            for (IntrospectedColumn introspectedColumn : cols) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                method.addParameter(parameter);
            }
            context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
            topLevelInterface.addImportedTypes(importedTypes);
            topLevelInterface.addMethod(method);
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
