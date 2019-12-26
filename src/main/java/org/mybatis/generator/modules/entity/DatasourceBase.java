package org.mybatis.generator.modules.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *<datasource Bean基础类>
 *
 * @since 2019/12/26 20:29:36
 * @table datasource
 * @mbggenerated
 */
public class DatasourceBase implements Serializable {
    private static final long serialVersionUID = 1577363376972L;

    /**
     * @mbggenerated
     *  id
     */
    private Long id;

    /**
     * @mbggenerated
     *  name
     */
    private String name;

    /**
     * @mbggenerated
     *  type
     */
    private String type;

    /**
     * @mbggenerated
     *  url
     */
    private String url;

    /**
     * @mbggenerated
     *  username
     */
    private String username;

    /**
     * @mbggenerated
     *  pwd
     */
    private String pwd;

    /**
     * @mbggenerated
     *  status
     */
    private String status;

    /**
     * @mbggenerated
     * 创建人 creator
     */
    private String creator;

    /**
     * @mbggenerated
     * 创建时间 create_time
     */
    private Date createTime;

    /**
     * @mbggenerated
     * 更新人 updator
     */
    private String updator;

    /**
     * @mbggenerated
     * 更新时间 update_time
     */
    private Date updateTime;

    /**
     * @return 获取  datasource.id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id 设置  datasource.id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 获取  datasource.name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 设置  datasource.name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return 获取  datasource.type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type 设置  datasource.type
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * @return 获取  datasource.url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 设置  datasource.url
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * @return 获取  datasource.username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username 设置  datasource.username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * @return 获取  datasource.pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd 设置  datasource.pwd
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * @return 获取  datasource.status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status 设置  datasource.status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return 获取 创建人 datasource.creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator 设置 创建人 datasource.creator
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * @return 获取 创建时间 datasource.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 设置 创建时间 datasource.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 获取 更新人 datasource.updator
     */
    public String getUpdator() {
        return updator;
    }

    /**
     * @param updator 设置 更新人 datasource.updator
     */
    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    /**
     * @return 获取 更新时间 datasource.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime 设置 更新时间 datasource.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}