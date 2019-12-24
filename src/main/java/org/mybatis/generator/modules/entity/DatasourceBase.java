package org.mybatis.generator.modules.entity;

import java.io.Serializable;

/**
 *
 *<datasource Bean基础类>
 *
 * @since 2019/11/15 10:33:12
 * @table datasource
 * @mbggenerated
 */
public class DatasourceBase implements Serializable {
    private static final long serialVersionUID = 1573785192748L;

    /**
     * @mbggenerated
     *  id
     */
    private Long id;

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
}