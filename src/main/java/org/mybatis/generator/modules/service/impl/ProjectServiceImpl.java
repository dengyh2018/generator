package org.mybatis.generator.modules.service.impl;

import org.mybatis.generator.modules.entity.Project;
import org.mybatis.generator.modules.mapper.ProjectMapper;
import org.mybatis.generator.modules.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <project Service基础类>
 *
 * @table project
 * @mbggenerated
 * @since 2019/12/26 18:34:48
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public Project selectByPrimaryKey(Long id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return projectMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean insertSelective(Project project) {
        return projectMapper.insertSelective(project) > 0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(Project project) {
        return projectMapper.updateByPrimaryKeySelective(project) > 0;
    }

    @Override
    public Project selectByConfigId_Ip(Long configId, String ip) {
        return projectMapper.selectByConfigId_Ip(configId, ip);
    }
}