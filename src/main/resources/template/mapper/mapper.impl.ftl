package com.aomygod.mapper.${packageName}.impl;

import org.springframework.stereotype.Component;

import com.aomygod.mapper.base.impl.BaseMapperImpl;
import com.aomygod.mapper.${packageName}.${entityName}Mapper;
import com.aomygod.${packageName}.pojo.${entityName};

/**
 * Created by ${createdBy} on ${createdDate}.
 */
 
@Component
public class ${entityName}MapperImpl extends BaseMapperImpl<${entityName}, ${idType}> implements ${entityName}Mapper {
	private static final String NAMESPACE = "com.aomygod.mapper.${packageName}.${entityName}Mapper.";
	
	@Override
	public String getMapperNamespace(String state) {
		 return NAMESPACE + state;
	}
}