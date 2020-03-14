package org.mybatis.generator.modules.mapper;

import org.mybatis.generator.modules.entity.Datasource;

/**
 *
 *<datasource Dao基础接口>
 *
 * @since 2020/03/14 10:54:49
 * @table datasource
 * @mbggenerated
 */
public interface DatasourceBaseMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @mbggenerated
     */
    int insertSelective(Datasource record);

    /**
     * @mbggenerated
     */
    Datasource selectByPrimaryKey(Long id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Datasource record);

    /**
     * @mbggenerated
     */
    @Deprecated
    int updateByPrimaryKey(Datasource record);
}