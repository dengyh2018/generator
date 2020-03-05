package org.mybatis.generator.modules.controller;

import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.modules.R;
import org.mybatis.generator.modules.constant.CommonConstant;
import org.mybatis.generator.modules.entity.Datasource;
import org.mybatis.generator.modules.entity.TableInfo;
import org.mybatis.generator.modules.service.DatasourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @RequestMapping(value = "/list", method = RequestMethod.POST)
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
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public R edit(@RequestBody Datasource datasource) {
        if (datasource.getId() != null) {
            datasourceService.updateByPrimaryKeySelective(datasource);
        } else {
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
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        Datasource datasource = datasourceService.selectByPrimaryKey(id);
        return R.ok().put("data", datasource);
    }

    /**
     * 数据源删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
    public R del(@PathVariable("id") Long id) {
        datasourceService.deleteByPrimaryKey(id);
        return R.ok();
    }

    /**
     * 数据源含有的表
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/listTable/{id}", method = RequestMethod.GET)
    public R listTable(@PathVariable("id") Long id) throws Exception {
        Datasource datasource = datasourceService.selectByPrimaryKey(id);
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration(
                CommonConstant.DataBaseDriver.getDescByValue(datasource.getType()), datasource.getUrl(), datasource.getUsername(), datasource.getPwd());
        //获取数据库名称
        List<String> tables = getMatchers(datasource.getUrl(), "\\w+/(\\S+)\\?");
        Connection connection = ConnectionFactory.getInstance().getConnection(jdbcConnectionConfiguration);
        PreparedStatement ps = connection.prepareStatement("select table_name from information_schema.tables where table_schema='" + tables.get(0) + "' and table_type='base table';");
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        List<TableInfo> tableList = new ArrayList<>();
        TableInfo tableInfo = null;
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getObject(i).toString());
                tableList.add(tableInfo);
            }
        }
        return R.ok().put("tableList", tableList);
    }

    /**
     * @param str   被匹配的字符串
     * @param regEx 正则表达式
     * @return 是否匹配成功
     */
    private static boolean regCheck(String str, String regEx) {
        if (str == null || str.equals("")) {
            return false;
        }
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;
    }

    public static List<String> getMatchers(String source, String regex) {
        if (source.indexOf("?") == -1) {
            source += "?";
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

    public static void main(String[] args) {
        List<String> tables = getMatchers("jdbc:mysql://o2omysql.m.aomygod.api:3306/o2o_college?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8", "\\w+/(\\S+)\\?");
        System.out.println(tables.toString());
    }

}