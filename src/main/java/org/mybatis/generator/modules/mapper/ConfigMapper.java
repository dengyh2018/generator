package org.mybatis.generator.modules.mapper;

import org.mybatis.generator.modules.entity.Config;

import java.util.List;

/**
 *
 *<config Dao自定义方法接口>
 *
 * @since 2019/12/25 10:52:21
 * @table config
 * @mbggenerated
 */
public interface ConfigMapper extends ConfigBaseMapper {

    List<Config> queryByDatasourceId(Long datasourceId);

}