package org.mybatis.generator.modules.controller;

import org.mybatis.generator.modules.R;
import org.mybatis.generator.modules.entity.Config;
import org.mybatis.generator.modules.entity.Datasource;
import org.mybatis.generator.modules.service.ConfigService;
import org.mybatis.generator.modules.service.DatasourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 编辑
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public R edit(@RequestBody Config config) {
        if(config.getId()!=null){
            configService.updateByPrimaryKeySelective(config);
        }else{
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
    @RequestMapping(value = "/getByDatasourceId/{datasourceId}",method = RequestMethod.POST)
    public R getByDatasourceId(@PathVariable("datasourceId") Long datasourceId) {
        List<Config> configList=configService.queryByDatasourceId(datasourceId);
        return R.ok().put("configList",configList);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        Config config=configService.selectByPrimaryKey(id);
        return R.ok().put("data",config);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/{id}",method = RequestMethod.POST)
    public R del(@PathVariable("id") Long id) {
        configService.deleteByPrimaryKey(id);
        return R.ok();
    }

}