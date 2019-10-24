package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MethodBean implements Comparable<MethodBean> {
	// 查询1
	// 更新2
	// 删除4
	private String methodType;

	private String resultForQuery;

	private String orderBy;

	/**
	 * 方法名称
	 */
	private String methodName;

	/**
	 * 方法需要的列
	 */

	private AndBean and;

	private List<OrBean> ors;

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public AndBean getAnd() {
		return and;
	}

	public void setAnd(AndBean and) {
		this.and = and;
	}

	public List<OrBean> getOrs() {
		if (ors == null || ors.isEmpty() || ors.size() == 1) {
			return ors;
		}
		Collections.sort(ors, new Comparator<OrBean>() {
			public int compare(OrBean o1, OrBean o2) {
				if (o1.getColumns() != null && o2.getColumns() != null && !o1.getColumns().isEmpty() && !o2.getColumns().isEmpty()) {
					return o1.getColumns().get(0).compareTo(o2.getColumns().get(0));
				}
				return 0;
			}
		});
		return ors;

	}

	public void setOrs(List<OrBean> ors) {
		this.ors = ors;
	}

	public String getResultForQuery() {
		return resultForQuery;
	}

	public void setResultForQuery(String resultForQuery) {
		this.resultForQuery = resultForQuery;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String toString() {
		return "MethodBean [methodType=" + methodType + ", resultForQuery=" + resultForQuery + ", orderBy=" + orderBy + ", methodName=" + methodName + ", and=" + and + ", ors="
				+ ors + "]";
	}

	public int compareTo(MethodBean oth) {
		this.toString().compareTo(oth.toString());
		return 0;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
