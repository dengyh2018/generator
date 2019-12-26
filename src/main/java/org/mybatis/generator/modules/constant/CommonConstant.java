package org.mybatis.generator.modules.constant;

/**
 * 鹈鹕学院常量
 */
public class CommonConstant {

    /**
     * 数据库驱动
     */
    public enum DataBaseDriver {
        MYSQL("mysql", "com.mysql.jdbc.Driver"),
        ORACLE("oracle", "oracle"),
        ;

        private String type;
        private String desc;

        DataBaseDriver(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }

        public static DataBaseDriver getEnumByValue(String type) {
            DataBaseDriver delFlags[] = DataBaseDriver.values();
            for (DataBaseDriver dataBaseDriver : delFlags) {
                if (dataBaseDriver.getType().equals(type)) {
                    return dataBaseDriver;
                }
            }
            return null;
        }

        public static String getDescByValue(String type) {
            DataBaseDriver delFlags[] = DataBaseDriver.values();
            for (DataBaseDriver dataBaseDriver : delFlags) {
                if (dataBaseDriver.getType().equals(type)) {
                    return dataBaseDriver.getDesc();
                }
            }
            return null;
        }
    }

}
