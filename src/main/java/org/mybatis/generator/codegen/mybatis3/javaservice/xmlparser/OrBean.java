package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class OrBean {
    private List<Column> columns=new LinkedList<Column>();

    public List<Column> getColumns() {
        Collections.sort(columns, new Comparator<Column>() {
            public int compare(Column o1, Column o2) {
                return o1.getName().compareTo(o2.getName());
            }
            
        });
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "OrBean [columns=" + columns + "]";
    }

    
    
}
