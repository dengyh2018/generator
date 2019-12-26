package org.mybatis.generator.modules.controller;

import org.mybatis.generator.modules.R;
import org.mybatis.generator.modules.entity.Project;
import org.mybatis.generator.modules.service.ProjectService;
import org.mybatis.generator.modules.utils.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 0253322_dengyh
 * @Date 2019/11/15
 * @description
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    /**
     * 编辑
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public R edit(@RequestBody Project project) {
        project.setIp(IPUtils.getLocalIp());
        if (project.getId() != null) {
            projectService.updateByPrimaryKeySelective(project);
        } else {
            projectService.insertSelective(project);
        }
        return R.ok();
    }

}