package org.mybatis.generator.modules.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *<project Bean基础类>
 *
 * @since 2019/12/26 20:29:37
 * @table project
 * @mbggenerated
 */
public class ProjectBase implements Serializable {
    private static final long serialVersionUID = 1577363377214L;

    /**
     * @mbggenerated
     *  id
     */
    private Long id;

    /**
     * @mbggenerated
     * 模块id config_id
     */
    private Long configId;

    /**
     * @mbggenerated
     * ip地址 ip
     */
    private String ip;

    /**
     * @mbggenerated
     * 原项目地址 old_address
     */
    private String oldAddress;

    /**
     * @mbggenerated
     * 新项目地址 new_address
     */
    private String newAddress;

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
     * @return 获取  project.id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id 设置  project.id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 获取 模块id project.config_id
     */
    public Long getConfigId() {
        return configId;
    }

    /**
     * @param configId 设置 模块id project.config_id
     */
    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    /**
     * @return 获取 ip地址 project.ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip 设置 ip地址 project.ip
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * @return 获取 原项目地址 project.old_address
     */
    public String getOldAddress() {
        return oldAddress;
    }

    /**
     * @param oldAddress 设置 原项目地址 project.old_address
     */
    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress == null ? null : oldAddress.trim();
    }

    /**
     * @return 获取 新项目地址 project.new_address
     */
    public String getNewAddress() {
        return newAddress;
    }

    /**
     * @param newAddress 设置 新项目地址 project.new_address
     */
    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress == null ? null : newAddress.trim();
    }

    /**
     * @return 获取 创建人 project.creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator 设置 创建人 project.creator
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * @return 获取 创建时间 project.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 设置 创建时间 project.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 获取 更新人 project.updator
     */
    public String getUpdator() {
        return updator;
    }

    /**
     * @param updator 设置 更新人 project.updator
     */
    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    /**
     * @return 获取 更新时间 project.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime 设置 更新时间 project.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}