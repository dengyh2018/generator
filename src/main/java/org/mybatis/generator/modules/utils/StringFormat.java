package org.mybatis.generator.modules.utils;

/**
 * @Author 0253322_dengyh
 * @Date 2019/12/26
 * @description
 */
public class StringFormat {

    public static String formatParam(String param) {
        return "\\$\\{" + param + "}";
    }

}
