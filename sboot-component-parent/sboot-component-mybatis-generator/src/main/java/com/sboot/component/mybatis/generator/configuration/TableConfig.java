package com.sboot.component.mybatis.generator.configuration;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class TableConfig {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类名
     */
    private String modelName;

    /**
     * service类名
     * 默认值 = modelName + Service 后缀
     */
    private String serviceName;

    /**
     * mapper.xml 对应的java mapper类名
     * 默认值 = modelName + Repository 后缀
     */
    private String repositoryName;

    /**
     * 自动生成的mapper.xml名
     * 默认值 = _前缀 + modelName + Repository 后缀
     */
    private String repositorySqlName;

    /**
     * 自定义的mapper.xml名
     * 默认值 = modelName + Repository 后缀
     */
    private String customRepositorySqlName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositorySqlName() {
        return repositorySqlName;
    }

    public void setRepositorySqlName(String repositorySqlName) {
        this.repositorySqlName = repositorySqlName;
    }

    public String getCustomRepositorySqlName() {
        return customRepositorySqlName;
    }

    public void setCustomRepositorySqlName(String customRepositorySqlName) {
        this.customRepositorySqlName = customRepositorySqlName;
    }
}
