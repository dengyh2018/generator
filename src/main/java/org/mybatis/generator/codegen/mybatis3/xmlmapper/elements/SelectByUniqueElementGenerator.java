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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class SelectByUniqueElementGenerator extends AbstractXmlElementGenerator {

	public SelectByUniqueElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {

		Map<String, List<IntrospectedColumn>> uniqueMap = introspectedTable.getUniqueMap();
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		for (List<IntrospectedColumn> cols : uniqueMap.values()) {
			XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
			answer.addAttribute(new Attribute("id", //$NON-NLS-1$
					introspectedTable.getSelectByUniqueStatementId() + introspectedTable.getMethodPartName(cols)));

			if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
				answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
						introspectedTable.getResultMapWithBLOBsId()));
			} else {
				answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
						introspectedTable.getBaseResultMapId()));
			}
			String parameterType;
			if (cols.size() > 1) {
				parameterType = "map"; //$NON-NLS-1$
			} else {
				parameterType = cols.get(0).getFullyQualifiedJavaType().toString();
			}

			answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
					parameterType));

			context.getCommentGenerator().addComment(answer);
			StringBuilder sb = new StringBuilder();
			sb.append("select "); //$NON-NLS-1$
			answer.addElement(new TextElement(sb.toString()));
			answer.addElement(getBaseColumnListElement(false));
			if (introspectedTable.hasBLOBColumns()) {
				answer.addElement(new TextElement(",")); //$NON-NLS-1$
				answer.addElement(getBlobColumnListElement(false));
			}

			sb.setLength(0);
			sb.append("from "); //$NON-NLS-1$
			sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
			answer.addElement(new TextElement(sb.toString()));
			boolean and = false;
			for (IntrospectedColumn introspectedColumn : cols) {
				sb.setLength(0);
				if (and) {
					sb.append("  and "); //$NON-NLS-1$
				} else {
					sb.append("where "); //$NON-NLS-1$
					and = true;
				}

				sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
				sb.append(" = "); //$NON-NLS-1$
				sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
				answer.addElement(new TextElement(sb.toString()));
			}

			if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable)) {
				parentElement.addElement(answer);
			}
		}

	}
}
