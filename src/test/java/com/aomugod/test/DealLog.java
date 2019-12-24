package com.aomugod.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DealLog {

	static String passport = "https://ssl.aomygod.com/passport";
	static String iString = "http://i.aomygod.com";

	public static void main(String[] args) {
		File file = new File("D:/i.aomygod.com.log");
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			
			
			Pattern pattern = Pattern.compile("正则表达式");
			Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World ");
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
			    matcher.appendReplacement(sbr, "Java");
			}
			matcher.appendTail(sbr);
			System.out.println(sbr.toString());

			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] lines = tempString.split(" ");
				
				
				//POST /passport/login HTTP/1.1
				//GET /bubugao-captcha-center/captcha/image?sessionId=cf7aa248403f477d996aacccb5f842da&bizType=1003&randcode=1375115919 HTTP/1.1
				
				String url = iString+lines[8];
				
				// 显示行号
				System.out.println("line " + line + ": " + url);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

	}

}
