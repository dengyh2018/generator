package org.mybatis.generator.modules.mapper;

import org.mybatis.generator.modules.entity.Config;

/**
 *
 *<config Dao基础接口>
 *
 * @since 2019/12/26 10:43:09
 * @table config
 * @mbggenerated
 */
public interface ConfigBaseMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @mbggenerated
     */
    int insertSelective(Config record);

    /**
     * @mbggenerated
     */
    Config selectByPrimaryKey(Long id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Config record);

    /**
     * @mbggenerated
     */
    @Deprecated
    int updateByPrimaryKey(Config record);
}