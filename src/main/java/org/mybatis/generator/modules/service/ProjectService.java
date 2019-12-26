package org.mybatis.generator.modules.service;

import org.mybatis.generator.modules.entity.Project;

/**
 * <project Service基础接口>
 *
 * @table project
 * @mbggenerated
 * @since 2019/12/26 18:34:48
 */
public interface ProjectService {
    Project selectByPrimaryKey(Long id);

    boolean deleteByPrimaryKey(Long id);

    boolean insertSelective(Project project);

    boolean updateByPrimaryKeySelective(Project project);

    Project selectByConfigId_Ip(Long configId, String ip);
}