package org.mybatis.generator.modules.controller;

import com.github.pagehelper.PageInfo;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.modules.R;
import org.mybatis.generator.modules.entity.Datasource;
import org.mybatis.generator.modules.service.DatasourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 0253322_dengyh
 * @Date 2019/11/15
 * @description
 */
@RestController
@RequestMapping("/datasource")
public class DatasourceController {

    private static final Logger logger = LoggerFactory.getLogger(DatasourceController.class);

    @Autowired
    private DatasourceService datasourceService;

    /**
     * 数据源列表
     *
     * @param datasource
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public R list(@RequestBody Datasource datasource) {
        List<Datasource> collegeClassCategoryList = datasourceService.list(datasource);
        return R.ok().put("list", collegeClassCategoryList);
    }

    /**
     * 数据源编辑
     *
     * @param datasource
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public R edit(@RequestBody Datasource datasource) {
        if(datasource.getId()!=null){
            datasourceService.updateByPrimaryKeySelective(datasource);
        }else{
            datasourceService.insertSelective(datasource);
        }
        return R.ok();
    }

    /**
     * 数据源详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        Datasource datasource=datasourceService.selectByPrimaryKey(id);
        return R.ok().put("data",datasource);
    }

    /**
     * 数据源删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/{id}",method = RequestMethod.POST)
    public R del(@PathVariable("id") Long id) {
        datasourceService.deleteByPrimaryKey(id);
        return R.ok();
    }

}