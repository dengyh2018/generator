package org.mybatis.generator.api;

public class ShellRunerConfig {
    /**
     * 模块
     */
    public String moKuai = "";

    //public String filePath = "";

    public String getMoKuai() {
        return moKuai;
    }

    //public ShellRunerConfig() {
    //super();
    //this.moKuai = moKuai;
    //}

    public ShellRunerConfig(String moKuai) {
        super();
        this.moKuai = moKuai;
    }

	/*public ShellRunerConfig(String moKuai, String filePath) {
		super();
		this.moKuai = moKuai;
		this.filePath = filePath;
	}*/

    public static String getPath(String path) {
        String getPath = ShellRunerConfig.class.getClassLoader().getResource(path).getFile().replaceFirst("^/", "");
        //System.out.println("getPath:"+getPath);
        return getPath;
    }

    public void setMoKuai(String moKuai) {
        this.moKuai = moKuai;
    }

    public String getDaoConfigPath() {
        return getPath(this.getMoKuai() + "/generatorConfig.xml");
    }

    public String getServiceCommonConfig() {
        //serviceConfig.xml
        return getPath(this.getMoKuai() + "/serviceConfig.xml");
    }

    public static String getDyhServiceConfig() {
        //serviceConfig.txt
        return getPath("commonXml/serviceConfig.txt");
    }

    public static String getDyhTableConfig() {
        //table.txt
        return getPath("commonXml/table.txt");
    }

    public static String getDyhGeneratorConfig() {
        //generatorConfig.txt
        return getPath("commonXml/generatorConfig.txt");
    }

    public static String getDyhJar() {
        //mysql-connector-java-5.1.30.jar
        return getPath("commonJar/mysql-connector-java-5.1.30.jar");
    }

    public static String getPropFilePath() {
        return getPath("config.properties");
    }

    public static String getServiceTableConfigPath() {
        return getPath("mybatis-core-tables");
    }

}
