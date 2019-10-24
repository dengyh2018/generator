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

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * @author Jeff Butler
 */
public class ExtendResultMapElementGenerator extends
        AbstractXmlElementGenerator {

    public ExtendResultMapElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        //dengyh mapper的baseresultMap
//        if (true) {
//            return;
//        }
        XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                introspectedTable.getBaseResultMapId()));

        String returnType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            returnType = introspectedTable.getRecordWithBLOBsType();
        } else {
            // table has BLOBs, but no BLOB class - BLOB fields must be
            // in the base class
            returnType = introspectedTable.getExtendRecordType();
        }

        answer.addAttribute(new Attribute("type", //$NON-NLS-1$
                returnType));

        if (!introspectedTable.isConstructorBased()) {

            String superType;
            if (introspectedTable.hasBLOBColumns()) {
                superType = introspectedTable.getMyBatis3JavaMapperType() + "." + introspectedTable.getResultMapWithBLOBsId();
            } else {
                superType = introspectedTable.getMyBatis3JavaMapperType() + "." + introspectedTable.getBaseResultMapId();
            }
            answer.addAttribute(new Attribute("extends", //$NON-NLS-1$
                    superType));

        }

        context.getCommentGenerator().addComment(answer);

        if (context.getPlugins()
                .sqlMapResultMapWithoutBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

}
