package org.mybatis.generator.modules.mapper;

import org.mybatis.generator.modules.entity.Project;

/**
 *
 *<project Dao基础接口>
 *
 * @since 2019/12/26 20:27:01
 * @table project
 * @mbggenerated
 */
public interface ProjectBaseMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @mbggenerated
     */
    int insertSelective(Project record);

    /**
     * @mbggenerated
     */
    Project selectByPrimaryKey(Long id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Project record);

    /**
     * @mbggenerated
     */
    @Deprecated
    int updateByPrimaryKey(Project record);
}