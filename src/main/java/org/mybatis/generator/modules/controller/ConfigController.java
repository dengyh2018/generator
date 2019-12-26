package org.mybatis.generator.modules.controller;

import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.modules.R;
import org.mybatis.generator.modules.entity.Config;
import org.mybatis.generator.modules.entity.Datasource;
import org.mybatis.generator.modules.entity.Project;
import org.mybatis.generator.modules.service.ConfigService;
import org.mybatis.generator.modules.service.DatasourceService;
import org.mybatis.generator.modules.service.ProjectService;
import org.mybatis.generator.modules.utils.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.mybatis.generator.modules.utils.StringFormat.formatParam;

/**
 * @Author 0253322_dengyh
 * @Date 2019/11/15
 * @description
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    private ProjectService projectService;

    /**
     * 编辑
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public R edit(@RequestBody Config config) {
        try {
            String tables = config.getTables();
            String models = config.getModels();
            String[] table = tables.split(",");
            String[] model = models.split(",");
            if (table.length != model.length) {
                return R.error("数据表与实体名称长度不相等");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("数据表与实体名称长度不相等");
        }
        if (config.getId() != null) {
            configService.updateByPrimaryKeySelective(config);
        } else {
            configService.insertSelective(config);
        }
        return R.ok();
    }

    /**
     * 列表，根据数据源ID
     *
     * @param datasourceId
     * @return
     */
    @RequestMapping(value = "/getByDatasourceId/{datasourceId}", method = RequestMethod.POST)
    public R getByDatasourceId(@PathVariable("datasourceId") Long datasourceId) {
        List<Config> configList = configService.queryByDatasourceId(datasourceId);
        return R.ok().put("configList", configList);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        Config config = configService.selectByPrimaryKey(id);
        return R.ok().put("data", config);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
    public R del(@PathVariable("id") Long id) {
        configService.deleteByPrimaryKey(id);
        return R.ok();
    }

    /**
     * 生成mybatis文件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/product/{id}", method = RequestMethod.POST)
    public R product(@PathVariable("id") Long id) {
        try {
            Config config = configService.selectByPrimaryKey(id);
            Project project = projectService.selectByConfigId_Ip(config.getId(), IPUtils.getLocalIp());
            String newAddress = project.getNewAddress().replaceAll("\\\\", "\\\\\\\\");
            config.setClientProject(config.getClientProject().replaceFirst(formatParam("address"), newAddress).replaceAll("\\\\", "/"));
            config.setXmlProject(config.getXmlProject().replaceFirst(formatParam("address"), newAddress).replaceAll("\\\\", "/"));
            config.setServiceProject(config.getServiceProject().replaceFirst(formatParam("address"), newAddress).replaceAll("\\\\", "/"));
            config.setModelProject(config.getModelProject().replaceFirst(formatParam("address"), newAddress).replaceAll("\\\\", "/"));
            Datasource datasource = datasourceService.selectByPrimaryKey(config.getDatasourceId());
            ShellRunner.autoProduct(config, datasource);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("d:\\sadasdasd".replaceAll("\\\\", "/"));
    }


}