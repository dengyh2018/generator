package org.mybatis.generator.modules.service.impl;

import javax.annotation.Resource;
import org.mybatis.generator.modules.mapper.DatasourceMapper;
import org.mybatis.generator.modules.entity.Datasource;
import org.mybatis.generator.modules.service.DatasourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *<datasource Service基础类>
 *
 * @since 2019/11/15 10:33:13
 * @table datasource
 * @mbggenerated
 */
@Service
public class DatasourceServiceImpl implements DatasourceService {
    @Resource
    private DatasourceMapper datasourceMapper;

    @Override
    public Datasource selectByPrimaryKey(Long id) {
        return datasourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return datasourceMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean insertSelective(Datasource datasource) {
        return datasourceMapper.insertSelective(datasource) > 0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(Datasource datasource) {
        return datasourceMapper.updateByPrimaryKeySelective(datasource) > 0;
    }

    @Override
    public List<Datasource> list(Datasource datasource){
        return datasourceMapper.list(datasource);
    }
}