package org.mybatis.generator.modules.service;

import org.mybatis.generator.modules.entity.Config;

import java.util.List;

/**
 *
 *<config Service基础接口>
 *
 * @since 2019/12/25 10:52:21
 * @table config
 * @mbggenerated
 */
public interface ConfigService {
    Config selectByPrimaryKey(Long id);

    boolean deleteByPrimaryKey(Long id);

    boolean insertSelective(Config config);

    boolean updateByPrimaryKeySelective(Config config);

    List<Config> queryByDatasourceId(Long datasourceId);
}