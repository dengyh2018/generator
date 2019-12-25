package org.mybatis.generator.modules.service.impl;

import javax.annotation.Resource;
import org.mybatis.generator.modules.entity.Config;
import org.mybatis.generator.modules.mapper.ConfigMapper;
import org.mybatis.generator.modules.service.ConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *<config Service基础类>
 *
 * @since 2019/12/25 10:52:21
 * @table config
 * @mbggenerated
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Resource
    private ConfigMapper configMapper;

    @Override
    public Config selectByPrimaryKey(Long id) {
        return configMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return configMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean insertSelective(Config config) {
        return configMapper.insertSelective(config) > 0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(Config config) {
        return configMapper.updateByPrimaryKeySelective(config) > 0;
    }

    @Override
    public List<Config> queryByDatasourceId(Long datasourceId) {
        return configMapper.queryByDatasourceId(datasourceId);
    }
}