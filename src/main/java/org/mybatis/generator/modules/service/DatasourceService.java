package org.mybatis.generator.modules.service;

import org.mybatis.generator.modules.entity.Datasource;

import java.util.List;

/**
 *
 *<datasource Service基础接口>
 *
 * @since 2019/11/15 10:33:12
 * @table datasource
 * @mbggenerated
 */
public interface DatasourceService {
    Datasource selectByPrimaryKey(Long id);

    boolean deleteByPrimaryKey(Long id);

    boolean insertSelective(Datasource datasource);

    boolean updateByPrimaryKeySelective(Datasource datasource);

    List<Datasource> list(Datasource datasource);

}