package com.aomygod.${packageName}.pojo;

import java.io.Serializable;
<#list imports as imp>
import ${imp};
</#list>

/**
 * Created by ${createdBy} on ${createdDate}.
 */
public class ${entityName} implements Serializable {

    <#list attrs as a>
    <#if a.columnComment?has_content>
    // ${a.columnComment}
    </#if>
    private ${a.type} ${a.name};
    </#list>

    <#list attrs as a>
    public void set${a.name?cap_first}(${a.type} ${a.name}){
        this.${a.name} = ${a.name};
    }

    public ${a.type} get${a.name?cap_first}(){
        return this.${a.name};
    }
    </#list>
}
