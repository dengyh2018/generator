/*
 *  Copyright 2006 The Apache Software Foundation
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
package org.mybatis.generator.api;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.logging.LogFactory;
import org.mybatis.generator.modules.constant.CommonConstant;
import org.mybatis.generator.modules.entity.Config;
import org.mybatis.generator.modules.entity.Datasource;
import org.mybatis.generator.modules.utils.MapStorage;
import org.mybatis.generator.modules.utils.ReadFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;
import static org.mybatis.generator.modules.utils.StringFormat.formatParam;

/**
 * This class allows the code generator to be run from the command line.
 *
 * @author Jeff Butler
 */
public class ShellRunner {

    public static void autoProduct(Config datasourceConfig, Datasource datasource) {
        autoProductDetails(datasourceConfig, datasource);
    }

    private static final String CONFIG_FILE = "-configfile"; //$NON-NLS-1$

    private static final String OVERWRITE = "-overwrite"; //$NON-NLS-1$

    private static final String CONTEXT_IDS = "-contextids"; //$NON-NLS-1$

    private static final String TABLES = "-tables"; //$NON-NLS-1$

    private static final String VERBOSE = "-verbose"; //$NON-NLS-1$

    private static final String FORCE_JAVA_LOGGING = "-forceJavaLogging"; //$NON-NLS-1$

    private static final String HELP_1 = "-?"; //$NON-NLS-1$

    private static final String HELP_2 = "-h"; //$NON-NLS-1$

    public static ShellRunerConfig shellRunerConfig = null;

    public static void autoProductDetails(Config datasourceConfig, Datasource datasource) {
        //存储后续使用
        MapStorage.datasourceMap.put("servicePackage", datasourceConfig.getServicePackage());
        MapStorage.datasourceMap.put("serviceProject", datasourceConfig.getServiceProject());
        MapStorage.datasourceMap.put("models", datasourceConfig.getModels());

        // 取出generatorConfig配置文件路径，现在需要手动生成dengyh
        String[] args = new String[]{CONFIG_FILE, OVERWRITE, VERBOSE};

        System.out.println(">>>>>>>>>>执行自动代码生成<<<<<<<<<<<");

        // 没有配置文件，程序退出
        if (args.length == 0) {
            usage();
            System.exit(0);
            throw new RuntimeException("生成mybatis文件失败"); // only to satisfy compiler, never returns
        }

        Map<String, String> arguments = parseCommandLine(args);

        if (arguments.containsKey(HELP_1)) {
            usage();
            System.exit(0);
            throw new RuntimeException("生成mybatis文件失败"); // only to satisfy compiler, never returns
        }

        if (!arguments.containsKey(CONFIG_FILE)) {
            writeLine(getString("RuntimeError.0")); //$NON-NLS-1$
            throw new RuntimeException("生成mybatis文件失败");
        }

//        String configfile = arguments.get(CONFIG_FILE);
//        System.out.println("generatorConfig xml==>" + configfile);

//        // 实例化dao生成配置文件
//        File configurationFile = new File(configfile);
//        // 配置文件不存在，程序退出
//        if (!configurationFile.exists()) {
//            writeLine(getString("RuntimeError.1", configfile)); //$NON-NLS-1$
//           throw new RuntimeException("生成mybatis文件失败");
//        }

        Set<String> fullyqualifiedTables = new HashSet<String>();
        if (arguments.containsKey(TABLES)) {
            StringTokenizer st = new StringTokenizer(arguments.get(TABLES), ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }

        Set<String> contextIds = new HashSet<String>();
        if (arguments.containsKey(CONTEXT_IDS)) {
            StringTokenizer st = new StringTokenizer(arguments.get(CONTEXT_IDS), ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contextIds.add(s);
                }
            }
        }

        List<String> warnings = new ArrayList<String>();

        try {
            //你干毛用的!!
            ConfigurationParser cp = new ConfigurationParser(warnings);

            //将generatorConfig.xml解析成List<Context> contexts
            //Configuration config = cp.parseConfiguration(configurationFile);
            InputStream is = null;
            try {
                StringBuilder sb = new StringBuilder("");
                //开始组装配置文件
                String generatorStr = ReadFile.readFileContent(ShellRunerConfig.getDyhGeneratorConfig());
                String tableStr = ReadFile.readFileContent(ShellRunerConfig.getDyhTableConfig());
                //获取需要转换的数据表
                String[] tables = datasourceConfig.getTables().split(",");
                String[] models = datasourceConfig.getModels().split(",");
                StringBuilder tableSb = new StringBuilder("");
                for (int i = 0; i < tables.length; i++) {
                    String table = tables[i];
                    tableSb.append(tableStr.replaceAll(formatParam("table"), table).replaceAll(formatParam("model"), upperCase(models[i])));
                }
                sb.append(generatorStr.replaceAll(formatParam("tableConfig"), tableSb.toString())
                        .replaceAll(formatParam("classPath"), ShellRunerConfig.getDyhJar())
                        .replaceAll(formatParam("driverClass"), CommonConstant.DataBaseDriver.getDescByValue(datasource.getType()))
                        .replaceAll(formatParam("url"), datasource.getUrl())
                        .replaceAll(formatParam("username"), datasource.getUsername())
                        .replaceAll(formatParam("pwd"), datasource.getPwd())
                        .replaceAll(formatParam("modelProject"), datasourceConfig.getModelProject())
                        .replaceAll(formatParam("modelPackage"), datasourceConfig.getModelPackage())
                        .replaceAll(formatParam("clientProject"), datasourceConfig.getClientProject())
                        .replaceAll(formatParam("clientPackage"), datasourceConfig.getClientPackage())
                        .replaceAll(formatParam("xmlProject"), datasourceConfig.getXmlProject())
                        .replaceAll(formatParam("xmlPackage"), datasourceConfig.getXmlPackage())
                );
                System.out.println(sb.toString());
                is = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException("文件字符转换出错");
            }
            Configuration config = cp.parseConfiguration(is);

            //你干毛用的!!
            DefaultShellCallback shellCallback = new DefaultShellCallback(arguments.containsKey(OVERWRITE));
            // mybatis生成规则校验
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            //数据库自省，基于自省，然后合并/保存生成的文件。
            ProgressCallback progressCallback = arguments.containsKey(VERBOSE) ? new VerboseProgressCallback() : null;
            //开始生成mybatis7788
            myBatisGenerator.generate(progressCallback, contextIds, fullyqualifiedTables);

        } catch (XMLParserException e) {
            writeLine(getString("Progress.3")); //$NON-NLS-1$
            writeLine();
            for (String error : e.getErrors()) {
                writeLine(error);
            }
            throw new RuntimeException("生成mybatis文件失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("生成mybatis文件失败");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("生成mybatis文件失败");
        } catch (InvalidConfigurationException e) {
            writeLine(getString("Progress.16")); //$NON-NLS-1$
            for (String error : e.getErrors()) {
                writeLine(error);
            }
            throw new RuntimeException("生成mybatis文件失败");
        } catch (InterruptedException e) {
            // ignore (will never happen with the DefaultShellCallback);
        }

        for (String warning : warnings) {
            writeLine(warning);
        }

        if (warnings.size() == 0) {
            writeLine(getString("Progress.4")); //$NON-NLS-1$
        } else {
            writeLine();
            writeLine(getString("Progress.5")); //$NON-NLS-1$
        }

    }

    public static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    private static void usage() {
        String lines = getString("Usage.Lines"); //$NON-NLS-1$
        int iLines = Integer.parseInt(lines);
        for (int i = 0; i < iLines; i++) {
            String key = "Usage." + i; //$NON-NLS-1$
            writeLine(getString(key));
        }
    }

    private static void writeLine(String message) {
        System.out.println(message);
    }

    private static void writeLine() {
        System.out.println();
    }

    private static Map<String, String> parseCommandLine(String[] args) {
        List<String> errors = new ArrayList<String>();
        Map<String, String> arguments = new HashMap<String, String>();

        for (int i = 0; i < args.length; i++) {
            if (CONFIG_FILE.equalsIgnoreCase(args[i])) {
                arguments.put(CONFIG_FILE, "Y");
            } else if (OVERWRITE.equalsIgnoreCase(args[i])) {
                arguments.put(OVERWRITE, "Y"); //$NON-NLS-1$
            } else if (VERBOSE.equalsIgnoreCase(args[i])) {
                arguments.put(VERBOSE, "Y"); //$NON-NLS-1$
            } else if (HELP_1.equalsIgnoreCase(args[i])) {
                arguments.put(HELP_1, "Y"); //$NON-NLS-1$
            } else if (HELP_2.equalsIgnoreCase(args[i])) {
                // put HELP_1 in the map here too - so we only
                // have to check for one entry in the mainline
                arguments.put(HELP_1, "Y"); //$NON-NLS-1$
            } else if (FORCE_JAVA_LOGGING.equalsIgnoreCase(args[i])) {
                LogFactory.forceJavaLogging();
            } else if (CONTEXT_IDS.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(CONTEXT_IDS, args[i + 1]);
                } else {
                    errors.add(getString("RuntimeError.19", CONTEXT_IDS)); //$NON-NLS-1$
                }
                i++;
            } else if (TABLES.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(TABLES, args[i + 1]);
                } else {
                    errors.add(getString("RuntimeError.19", TABLES)); //$NON-NLS-1$
                }
                i++;
            } else {
                errors.add(getString("RuntimeError.20", args[i])); //$NON-NLS-1$
            }
        }

        if (!errors.isEmpty()) {
            for (String error : errors) {
                writeLine(error);
            }
            System.exit(-1);
        }

        return arguments;
    }
}
