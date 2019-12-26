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
package org.mybatis.generator.codegen.mybatis3;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.ExtendJavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javaservice.JavaControlGenerator;
import org.mybatis.generator.codegen.mybatis3.javaservice.JavaServiceGenerator;
import org.mybatis.generator.codegen.mybatis3.javaservice.JavaServiceImplGenerator;
import org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser.ConfigBean;
import org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser.ConfigLoader;
import org.mybatis.generator.codegen.mybatis3.model.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.ObjectFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author Jeff Butler
 */
public class IntrospectedTableMyBatis3Impl extends IntrospectedTable {
    protected List<AbstractJavaGenerator> javaModelGenerators;
    protected List<AbstractJavaGenerator> clientGenerators;
    protected List<AbstractXmlGenerator> xmlMapperGenerators;
    protected AbstractXmlGenerator xmlMapperGenerator;
    private CompilationUnit fields;

    public IntrospectedTableMyBatis3Impl() {
        super(TargetRuntime.MYBATIS3);
        javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
        clientGenerators = new ArrayList<AbstractJavaGenerator>();
        xmlMapperGenerators = new ArrayList<AbstractXmlGenerator>();
    }

    @Override
    public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
        calculateJavaModelGenerators(warnings, progressCallback);

        AbstractJavaClientGenerator javaClientGenerator = calculateClientGenerators(warnings, progressCallback);

        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);

        // 创建Extend Dao
        AbstractJavaClientGenerator extendJavaClientGenerator = calculateExtendClientGenerators(warnings, progressCallback);
        // 创建Extend Dao XML
        calculateXmlMapperGenerator(extendJavaClientGenerator, warnings, progressCallback);

        // 生成Service方法
        calculateJavaServiceGenerators(warnings, progressCallback);

        // 生成ServiceImpl方法
        calculateJavaServiceImplGenerators(warnings, progressCallback);

        // 生成Control方法
        calculateJavaMVCControlGenerators(warnings, progressCallback);

    }

    private void calculateJavaMVCControlGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new JavaControlGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
    }

    private void calculateJavaServiceGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new JavaServiceGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
    }

    private void calculateJavaServiceImplGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new JavaServiceImplGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
    }

    private AbstractJavaClientGenerator calculateExtendClientGenerators(List<String> warnings, ProgressCallback progressCallback) {

        if (!rules.generateJavaClient()) {
            return null;
        }

        AbstractJavaClientGenerator javaGenerator = createExtendJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }

        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        clientGenerators.add(javaGenerator);

        return javaGenerator;
    }

    protected AbstractJavaClientGenerator createExtendJavaClientGenerator() {

        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }

        String type = context.getJavaClientGeneratorConfiguration().getConfigurationType();

        AbstractJavaClientGenerator javaGenerator = null;
        if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new ExtendJavaMapperGenerator();
        } else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            // javaGenerator = new MixedClientGenerator();
        } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            // javaGenerator = new AnnotatedClientGenerator();
        } else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            // javaGenerator = new JavaMapperGenerator();
        } else {
            javaGenerator = (AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
        }

        return javaGenerator;
    }

    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings,
                                               ProgressCallback progressCallback) {
        AbstractXmlGenerator xmlMapperGenerator = null;
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration() != null) {
                xmlMapperGenerator = new XMLMapperGenerator();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }

        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
        xmlMapperGenerators.add(xmlMapperGenerator);
    }

    protected void calculateExtendXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings,
                                                     ProgressCallback progressCallback) {
        AbstractXmlGenerator xmlMapperGenerator = null;
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration() != null) {
                xmlMapperGenerator = new XMLMapperGenerator();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }

        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);

        xmlMapperGenerators.add(xmlMapperGenerator);
    }

    /**
     * @param warnings
     * @param progressCallback
     * @return true if an XML generator is required
     */
    protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (!rules.generateJavaClient()) {
            return null;
        }

        AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }

        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        clientGenerators.add(javaGenerator);

        return javaGenerator;
    }

    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }

        String type = context.getJavaClientGeneratorConfiguration().getConfigurationType();

        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator();
        } else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new MixedClientGenerator();
        } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new AnnotatedClientGenerator();
        } else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator();
        } else {
            javaGenerator = (AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
        }

        return javaGenerator;
    }

    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generateExampleClass()) {
            AbstractJavaGenerator javaGenerator = new ExampleGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generatePrimaryKeyClass()) {
            AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new ExtendRecordGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateRecordWithBLOBsClass()) {
            AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
    }

    protected void initializeAbstractGenerator(AbstractGenerator abstractGenerator, List<String> warnings, ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        /**
         * 生成Java文件
         */
        for (AbstractJavaGenerator javaGenerator : javaModelGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();

            for (CompilationUnit compilationUnit : compilationUnits) {

                //dengyh
                //如果是实体类base，踢掉几个字段
                String baseShortName = compilationUnit.getType().getShortName();
                if (baseShortName.endsWith("Base")) {
                    //List<Field> fields= this.fields;
                }

                GeneratedJavaFile gjf = null;

                if (javaGenerator instanceof JavaServiceGenerator || javaGenerator instanceof JavaServiceImplGenerator) {
                    ConfigBean configBean = ConfigLoader.load(ShellRunner.shellRunerConfig);
                    String targetProject = configBean.getJavaServiceGenerator().getTargetProject();
                    gjf = new GeneratedJavaFile(compilationUnit, targetProject, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                            context.getJavaFormatter());
                    File directory = null;
                    try {
                        directory = context.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
                    } catch (ShellException e) {
                        e.printStackTrace();
                    }
                    // Service不进行service重复生成
                    File dir = new File(directory.getPath() + directory.separator + gjf.getFileName());
                    if (dir.exists()) {
                        System.out.println(String.format("Existing file %s was not overwritten", dir.getAbsolutePath()));
                        continue;
                    }
                }
                // 其他代码生成
                else {
                    gjf = new GeneratedJavaFile(compilationUnit, context.getJavaModelGeneratorConfiguration().getTargetProject(),
                            context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
                }

                if (javaGenerator instanceof ExtendRecordGenerator) {
                    try {
                        File directory = this.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());

                        File targetFile = new File(directory, gjf.getFileName());
                        if (!targetFile.exists()) {
                            answer.add(gjf);
                        }
                    } catch (ShellException e) {
                        e.printStackTrace();
                    }
                } else {
                    answer.add(gjf);
                }

            }
        }

        /**
         * 生成Dao文件
         */
        for (AbstractJavaGenerator javaGenerator : clientGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {

                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit, context.getJavaClientGeneratorConfiguration().getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());

                if (javaGenerator instanceof ExtendJavaMapperGenerator) {
                    try {
                        File directory = this.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());

                        File targetFile = new File(directory, gjf.getFileName());
                        if (!targetFile.exists()) {
                            answer.add(gjf);
                        }
                    } catch (ShellException e) {
                        e.printStackTrace();
                    }
                } else {
                    answer.add(gjf);
                }
            }
        }

        return answer;
    }

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

        if (xmlMapperGenerators != null) {
            if (xmlMapperGenerators.size() == 2) {
                AbstractXmlGenerator xmlMapperGeneratori = xmlMapperGenerators.get(0);
                Document document = xmlMapperGeneratori.getDocument();
                GeneratedXmlFile gxf = new GeneratedXmlFile(document, getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                        context.getSqlMapGeneratorConfiguration().getTargetProject(), true, context.getXmlFormatter());
                if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                    answer.add(gxf);
                }

                // 生成扩展XML文件
                String filePath = context.getSqlMapGeneratorConfiguration().getTargetProject();
                filePath = PropertiesUtils.parseValue(filePath);

                AbstractXmlGenerator xmlMapperGeneratorj = xmlMapperGenerators.get(1);
                Document documentj = xmlMapperGeneratorj.getDocument();
                GeneratedXmlFile gxfj = new GeneratedXmlFile(documentj, getMyBatis3ExtendXmlMapperFileName(), getMyBatis3ExtendXmlMapperPackage(),
                        filePath, true, context.getXmlFormatter());
                if (context.getPlugins().sqlMapGenerated(gxfj, this)) {
                    answer.add(gxfj);
                }
            }

        }

        return answer;
    }

    private String getMyBatis3ExtendXmlMapperFileName() {
        return internalAttributes.get(InternalAttribute.ATTR_MYBATIS3_EXTEND_XML_MAPPER_FILE_NAME);
    }

    @Override
    public int getGenerationSteps() {
        return javaModelGenerators.size() + clientGenerators.size() + xmlMapperGenerators.size();
        // (xmlMapperGenerators == null ? 0 : 1);

    }

    @Override
    public boolean isJava5Targeted() {
        return true;
    }

    @Override
    public boolean requiresXMLGenerator() {
        AbstractJavaClientGenerator javaClientGenerator = createJavaClientGenerator();

        if (javaClientGenerator == null) {
            return false;
        } else {
            return javaClientGenerator.requiresXMLGenerator();
        }
    }

    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        // targetProject is interpreted as a directory that must exist
        //
        // targetPackage is interpreted as a sub directory, but in package
        // format (with dots instead of slashes). The sub directory will be
        // created
        // if it does not already exist

        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new ShellException(getString("Warning.9", //$NON-NLS-1$
                    targetProject));
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(getString("Warning.10", //$NON-NLS-1$
                        directory.getAbsolutePath()));
            }
        }

        return directory;
    }
}
