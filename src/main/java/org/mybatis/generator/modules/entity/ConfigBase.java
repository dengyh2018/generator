package org.mybatis.generator.modules.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *<config Bean基础类>
 *
 * @since 2019/12/26 20:27:01
 * @table config
 * @mbggenerated
 */
public class ConfigBase implements Serializable {
    private static final long serialVersionUID = 1577363221307L;

    /**
     * @mbggenerated
     *  id
     */
    private Long id;

    /**
     * @mbggenerated
     * 数据源id datasource_id
     */
    private Long datasourceId;

    /**
     * @mbggenerated
     * 模块名称 name
     */
    private String name;

    /**
     * @mbggenerated
     * 数据表 tables
     */
    private String tables;

    /**
     * @mbggenerated
     * 实体名称 models
     */
    private String models;

    /**
     * @mbggenerated
     * xml所在项目/精确到包路径(不包含包路径) xml_project
     */
    private String xmlProject;

    /**
     * @mbggenerated
     * xml包路径 xml_package
     */
    private String xmlPackage;

    /**
     * @mbggenerated
     * entity所在项目/精确到包路径(不包含包路径) model_project
     */
    private String modelProject;

    /**
     * @mbggenerated
     * entity包路径 model_package
     */
    private String modelPackage;

    /**
     * @mbggenerated
     * dao所在项目/精确到包路径(不包含包路径) client_project
     */
    private String clientProject;

    /**
     * @mbggenerated
     * dao包路径 client_package
     */
    private String clientPackage;

    /**
     * @mbggenerated
     * service所在项目/精确到包路径(不包含包路径) service_project
     */
    private String serviceProject;

    /**
     * @mbggenerated
     * service包路径 service_package
     */
    private String servicePackage;

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
     * @return 获取  config.id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id 设置  config.id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 获取 数据源id config.datasource_id
     */
    public Long getDatasourceId() {
        return datasourceId;
    }

    /**
     * @param datasourceId 设置 数据源id config.datasource_id
     */
    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    /**
     * @return 获取 模块名称 config.name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 设置 模块名称 config.name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return 获取 数据表 config.tables
     */
    public String getTables() {
        return tables;
    }

    /**
     * @param tables 设置 数据表 config.tables
     */
    public void setTables(String tables) {
        this.tables = tables == null ? null : tables.trim();
    }

    /**
     * @return 获取 实体名称 config.models
     */
    public String getModels() {
        return models;
    }

    /**
     * @param models 设置 实体名称 config.models
     */
    public void setModels(String models) {
        this.models = models == null ? null : models.trim();
    }

    /**
     * @return 获取 xml所在项目/精确到包路径(不包含包路径) config.xml_project
     */
    public String getXmlProject() {
        return xmlProject;
    }

    /**
     * @param xmlProject 设置 xml所在项目/精确到包路径(不包含包路径) config.xml_project
     */
    public void setXmlProject(String xmlProject) {
        this.xmlProject = xmlProject == null ? null : xmlProject.trim();
    }

    /**
     * @return 获取 xml包路径 config.xml_package
     */
    public String getXmlPackage() {
        return xmlPackage;
    }

    /**
     * @param xmlPackage 设置 xml包路径 config.xml_package
     */
    public void setXmlPackage(String xmlPackage) {
        this.xmlPackage = xmlPackage == null ? null : xmlPackage.trim();
    }

    /**
     * @return 获取 entity所在项目/精确到包路径(不包含包路径) config.model_project
     */
    public String getModelProject() {
        return modelProject;
    }

    /**
     * @param modelProject 设置 entity所在项目/精确到包路径(不包含包路径) config.model_project
     */
    public void setModelProject(String modelProject) {
        this.modelProject = modelProject == null ? null : modelProject.trim();
    }

    /**
     * @return 获取 entity包路径 config.model_package
     */
    public String getModelPackage() {
        return modelPackage;
    }

    /**
     * @param modelPackage 设置 entity包路径 config.model_package
     */
    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage == null ? null : modelPackage.trim();
    }

    /**
     * @return 获取 dao所在项目/精确到包路径(不包含包路径) config.client_project
     */
    public String getClientProject() {
        return clientProject;
    }

    /**
     * @param clientProject 设置 dao所在项目/精确到包路径(不包含包路径) config.client_project
     */
    public void setClientProject(String clientProject) {
        this.clientProject = clientProject == null ? null : clientProject.trim();
    }

    /**
     * @return 获取 dao包路径 config.client_package
     */
    public String getClientPackage() {
        return clientPackage;
    }

    /**
     * @param clientPackage 设置 dao包路径 config.client_package
     */
    public void setClientPackage(String clientPackage) {
        this.clientPackage = clientPackage == null ? null : clientPackage.trim();
    }

    /**
     * @return 获取 service所在项目/精确到包路径(不包含包路径) config.service_project
     */
    public String getServiceProject() {
        return serviceProject;
    }

    /**
     * @param serviceProject 设置 service所在项目/精确到包路径(不包含包路径) config.service_project
     */
    public void setServiceProject(String serviceProject) {
        this.serviceProject = serviceProject == null ? null : serviceProject.trim();
    }

    /**
     * @return 获取 service包路径 config.service_package
     */
    public String getServicePackage() {
        return servicePackage;
    }

    /**
     * @param servicePackage 设置 service包路径 config.service_package
     */
    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage == null ? null : servicePackage.trim();
    }

    /**
     * @return 获取 创建人 config.creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator 设置 创建人 config.creator
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * @return 获取 创建时间 config.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 设置 创建时间 config.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 获取 更新人 config.updator
     */
    public String getUpdator() {
        return updator;
    }

    /**
     * @param updator 设置 更新人 config.updator
     */
    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    /**
     * @return 获取 更新时间 config.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime 设置 更新时间 config.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}