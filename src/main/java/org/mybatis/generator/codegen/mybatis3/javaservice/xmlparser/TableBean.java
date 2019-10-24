package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableBean {
	private String tableName;

	private List<MethodBean> methods;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<MethodBean> getMethods() {
		if (methods == null || methods.size() == 1) {
			return methods;
		}
		Collections.sort(methods, new Comparator<MethodBean>() {

			public int compare(MethodBean o1, MethodBean o2) {
				return o1.compareTo(o2);
			}
		});

		return methods;
	}

	public void setMethods(List<MethodBean> methods) {
		this.methods = methods;
	}

	@Override
	public String toString() {
		return "TableBean [tableName=" + tableName + ", methods=" + methods + "]";
	}

}
