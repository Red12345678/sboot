package com.sboot.component.mybatis.generator.model;

import com.sboot.component.database.basic.model.mybatis.BaseEntity;
import com.sboot.component.database.basic.model.mybatis.BaseRepository;
import com.sboot.component.database.basic.model.mybatis.BaseService;
import com.sboot.component.database.metadata.Column;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class DataModel {

    private String tableName;

    private String modelName;

    private String modelPackage;

    private String repositoryName;

    private String repositoryPackage;

    private String serviceName;

    private String servicePackage;

    private String repositorySqlName;

    private String customRepositorySqlName;

    private List<Column> columns;

    private Column primaryKey;

    public Set<String> getModelImportTypes(){
        Set<String> importTypes = new TreeSet<>();
        importTypes.add(getModelExtendType().getName());
        this.getColumns().forEach(column -> {
            if(!column.getJavaType().getPackage().getName().startsWith("java.lang")){
                importTypes.add(column.getJavaType().getName());
            }
        });
        return importTypes;
    }

    public Class getModelExtendType(){
        return BaseEntity.class;
    }

    public String getModelExtends(){
        String primaryKeyJavaTypeSimpleName = this.getPrimaryKey().getJavaType().getSimpleName();
        return getModelExtendType().getSimpleName() + "<" + primaryKeyJavaTypeSimpleName + ">";
    }

    public Set<String> getRepositoryImportTypes(){
        Set<String> importTypes = new TreeSet<>();
        importTypes.add(getRepositoryExtendType().getName());
        importTypes.add(String.join(".", getModelPackage(), getModelName()));
        return importTypes;
    }

    public String getRepositoryExtends(){
        // BaseRepository<User, String>
        String primaryKeyJavaTypeSimpleName = this.getPrimaryKey().getJavaType().getSimpleName();
        return getRepositoryExtendType().getSimpleName() + "<" + this.getModelName() + ", " + primaryKeyJavaTypeSimpleName + ">";
    }

    public Class getRepositoryExtendType(){
        return BaseRepository.class;
    }

    public Set<String> getServiceImportTypes(){
        Set<String> importTypes = new TreeSet<>();
        importTypes.add(getServiceExtendType().getName());
        importTypes.add(String.join(".", getModelPackage(), getModelName()));
        importTypes.add(String.join(".", getRepositoryPackage(), getRepositoryName()));
        return importTypes;
    }

    public String getServiceExtends(){
        // UserService extends BaseService<UserRepository, User, String>
        String primaryKeyJavaTypeSimpleName = this.getPrimaryKey().getJavaType().getSimpleName();
        return getServiceExtendType().getSimpleName() + "<" + this.getRepositoryName() + ", " + this.getModelName()  + ", " + primaryKeyJavaTypeSimpleName  + ">";
    }

    public Class getServiceExtendType(){
        return BaseService.class;
    }




    public String getColumnNames(){
        return String.join(",", this.getColumns().stream().map(Column::getName).collect(Collectors.toList()));
    }

    public List<Column> getColumnsWithoutPrimaryKey(){
        return this.getColumns().stream().filter(column -> {
            return !Objects.equals(column.getName(), this.getPrimaryKey().getName());
        }).collect(Collectors.toList());
    }





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

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Column getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryPackage() {
        return repositoryPackage;
    }

    public void setRepositoryPackage(String repositoryPackage) {
        this.repositoryPackage = repositoryPackage;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
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

    public static class Column {
        private String name;
        private String remarks;
        private String property;
        private String jdbcType;
        private Class javaType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getJdbcType() {
            return jdbcType;
        }

        public void setJdbcType(String jdbcType) {
            this.jdbcType = jdbcType;
        }

        public Class getJavaType() {
            return javaType;
        }

        public void setJavaType(Class javaType) {
            this.javaType = javaType;
        }
    }
}
