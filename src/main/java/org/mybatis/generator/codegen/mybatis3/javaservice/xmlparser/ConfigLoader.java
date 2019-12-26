package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

import com.thoughtworks.xstream.XStream;
import org.mybatis.generator.api.ShellRunerConfig;
import org.mybatis.generator.modules.utils.MapStorage;
import org.mybatis.generator.modules.utils.ReadFile;

import java.io.File;
import java.util.*;

import static org.mybatis.generator.modules.utils.StringFormat.formatParam;

public class ConfigLoader {

    /**
     * dengyh service接口类/实现类生成
     *
     * @param shellRunerConfig
     * @return
     */
    public static ConfigBean load(ShellRunerConfig shellRunerConfig) {

        XStream stream = new XStream();
        stream.alias("root", ConfigBean.class);
        stream.addImplicitCollection(ConfigBean.class, "tables");

        stream.alias("javaServiceGenerator", ServiceBean.class);
        stream.useAttributeFor(ServiceBean.class, "interfacePackage");
        stream.useAttributeFor(ServiceBean.class, "classPackage");
        stream.useAttributeFor(ServiceBean.class, "targetProject");

        stream.alias("controlGenerator", ControlBean.class);
        stream.useAttributeFor(ControlBean.class, "targetPackage");

        //Object object = stream.fromXML(new File(shellRunerConfig.getServiceCommonConfig()));
        String serviceStr = ReadFile.readFileContent(ShellRunerConfig.getDyhServiceConfig());
        serviceStr = serviceStr.replaceAll(formatParam("serviceProject"), MapStorage.datasourceMap.get("serviceProject"))
                .replaceAll(formatParam("servicePackage"), MapStorage.datasourceMap.get("servicePackage"));
        System.out.println(serviceStr);
        Object object = stream.fromXML(serviceStr);

        ConfigBean config = (ConfigBean) object;

        File floder = new File(ShellRunerConfig.getServiceTableConfigPath());

        List<File> xmlFiles = new ArrayList<File>();
        listFiles(floder, xmlFiles, ".xml");

        for (File file : xmlFiles) {

            XStream tableStream = new XStream();
            tableStream.alias("table", TableBean.class);
            tableStream.useAttributeFor(TableBean.class, "tableName");
            tableStream.addImplicitCollection(TableBean.class, "methods");

            tableStream.alias("method", MethodBean.class);
            tableStream.useAttributeFor(MethodBean.class, "methodType");
            tableStream.useAttributeFor(MethodBean.class, "orderBy");
            tableStream.useAttributeFor(MethodBean.class, "methodName");
            tableStream.useAttributeFor(MethodBean.class, "resultForQuery");
            // tableStream.addImplicitCollection(MethodBean.class, "ors");
            // tableStream.addImplicitCollection(MethodBean.class, "ands");

            tableStream.alias("column", Column.class);
            tableStream.useAttributeFor(Column.class, "name");
            tableStream.useAttributeFor(Column.class, "paramType");

            tableStream.alias("or", OrBean.class);
            tableStream.addImplicitCollection(OrBean.class, "columns");

            tableStream.alias("and", AndBean.class);
            tableStream.addImplicitCollection(AndBean.class, "columns");
            TableBean tableBean = (TableBean) tableStream.fromXML(file);
            if (config.getTables() == null) {
                config.setTables(new HashSet<TableBean>());
            }
            config.getTables().add(tableBean);
        }

        return config;
    }

    public static void listFiles(File parentFolder, List<File> xmlFiles, String suffic) {
        if (parentFolder.isFile()) {
            if (parentFolder.getName().endsWith(suffic)) {
                xmlFiles.add(parentFolder);
            }
        } else {
            File[] sonFolders = parentFolder.listFiles();
            for (File file : sonFolders) {
                listFiles(file, xmlFiles, suffic);
            }
        }
    }

    public static void main(String[] args) {
        ConfigBean conf = load(new ShellRunerConfig("activity"));
        System.out.println(conf);
        Set<TableBean> tables = conf.getTables();
        Iterator<TableBean> it = tables.iterator();
        while (it.hasNext()) {
            TableBean tableBean = it.next();
            System.out.println(tableBean);
            Iterator<MethodBean> methodsIt = tableBean.getMethods().iterator();
            while (methodsIt.hasNext()) {
                MethodBean methodBean = methodsIt.next();
                System.out.println(methodBean);

                Iterator<Column> columnsIt = methodBean.getAnd().getColumns().iterator();
                while (columnsIt.hasNext()) {
                    Column columnBean = columnsIt.next();
                    System.out.println(columnBean);
                }

                if (methodBean.getOrs() != null && !methodBean.getOrs().isEmpty()) {
                    for (int i = 0; i < methodBean.getOrs().size(); i++) {
                        Iterator<Column> columnsIt2 = methodBean.getOrs().get(i).getColumns().iterator();
                        while (columnsIt2.hasNext()) {
                            Column columnBean = columnsIt2.next();
                            System.out.println(columnBean);

                        }
                    }
                }

            }

        }
    }
}
