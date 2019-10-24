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

	public static String getPath(String path){
	    String  getPath=ShellRunerConfig.class.getClassLoader().getResource(path).getFile().replaceFirst("^/","");
        System.out.println("getPath:"+getPath);
		return getPath;
		//return ""+path;
	}

	public void setMoKuai(String moKuai) {
		this.moKuai = moKuai;
	}

	public String getDaoConfigPath() {
		return getPath(this.getMoKuai() + "/generatorConfig.xml");
	}

	public String getServiceCommonConfig() {
		//单独文件serviceConfig.xml
		//return getPath(this.getMoKuai() + "/serviceConfig.xml");
		//是否能合并在generatorConfig.xml??
		return getPath(this.getMoKuai() + "/serviceConfig.xml");
	}

	public static String getPropFilePath() {
		return getPath("config.properties");
	}

	public String getServiceTableConfigPath() {
		return getPath("mybatis-core-tables");
	}

}
