package com.sboot.component.mybatis.generator.configuration;

import com.sboot.component.database.Jdbc;
import org.omg.CORBA.Object;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class Configuration {

    private Jdbc jdbc;
    private File templateDirectory;

    private List<TableConfig> tableConfigs;

    private GenerateJavaFile modelFile;
    private GenerateJavaFile repositoryFile;
    private GenerateJavaFile serviceFile;
    private GenerateXmlFile repositorySqlFile;
    private GenerateXmlFile repositoryCustomSqlFile;

    public Jdbc getJdbc() {
        return jdbc;
    }

    public void setJdbc(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public File getTemplateDirectory() {
        return templateDirectory;
    }

    public void setTemplateDirectory(File templateDirectory) {
        this.templateDirectory = templateDirectory;
    }

    public GenerateJavaFile getModelFile() {
        return modelFile;
    }

    public void setModelFile(GenerateJavaFile modelFile) {
        this.modelFile = modelFile;
    }

    public GenerateJavaFile getRepositoryFile() {
        return repositoryFile;
    }

    public void setRepositoryFile(GenerateJavaFile repositoryFile) {
        this.repositoryFile = repositoryFile;
    }

    public GenerateJavaFile getServiceFile() {
        return serviceFile;
    }

    public void setServiceFile(GenerateJavaFile serviceFile) {
        this.serviceFile = serviceFile;
    }

    public GenerateXmlFile getRepositorySqlFile() {
        return repositorySqlFile;
    }

    public void setRepositorySqlFile(GenerateXmlFile repositorySqlFile) {
        this.repositorySqlFile = repositorySqlFile;
    }

    public GenerateXmlFile getRepositoryCustomSqlFile() {
        return repositoryCustomSqlFile;
    }

    public void setRepositoryCustomSqlFile(GenerateXmlFile repositoryCustomSqlFile) {
        this.repositoryCustomSqlFile = repositoryCustomSqlFile;
    }

    public List<TableConfig> getTableConfigs() {
        return tableConfigs;
    }

    public void setTableConfigs(List<TableConfig> tableConfigs) {
        this.tableConfigs = tableConfigs;
    }

    public Map<String, TableConfig> getTableConfigMap(){
        Map<String, TableConfig> map = new HashMap<>();
        if(!Objects.isNull(this.tableConfigs) && !this.tableConfigs.isEmpty()){
            this.tableConfigs.forEach(tableConfig -> map.put(tableConfig.getTableName(), tableConfig));
        }
        return map;
    }
}
