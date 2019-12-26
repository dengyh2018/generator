package org.mybatis.generator.modules.mapper;

import org.apache.ibatis.annotations.Param;
import org.mybatis.generator.modules.entity.Project;

/**
 * <project Dao自定义方法接口>
 *
 * @table project
 * @mbggenerated
 * @since 2019/12/26 18:34:48
 */
public interface ProjectMapper extends ProjectBaseMapper {

    Project selectByConfigId_Ip(@Param("configId") Long configId, @Param("ip") String ip);

}