package org.mybatis.generator.modules.controller;

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
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R queryCategoryByParentId(@RequestBody Datasource datasource) {
        List<Datasource> collegeClassCategoryList = datasourceService.list(datasource);
        return R.ok().put("data", collegeClassCategoryList);
    }

}