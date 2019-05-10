package com.sboot.component.mybatis.generator;


import com.alibaba.fastjson.JSON;
import com.sboot.component.database.helper.DatabaseMetaDataHelper;
import com.sboot.component.database.JdbcConnectionBuilder;
import com.sboot.component.database.metadata.Column;
import com.sboot.component.database.metadata.PrimaryKey;
import com.sboot.component.database.metadata.Table;
import com.sboot.component.mybatis.generator.configuration.Configuration;
import com.sboot.component.mybatis.generator.configuration.GenerateFile;
import com.sboot.component.mybatis.generator.configuration.TableConfig;
import com.sboot.component.mybatis.generator.model.DataModel;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tuozq
 * @description: model、service、repository 代码生成工具
 * @date 2019/5/10.
 */
public class MybatisGenerator {

    private static final Logger log = LoggerFactory.getLogger(MybatisGenerator.class);

    /**
     * freemarker configuration
     */
    private freemarker.template.Configuration templateConfiguration;

    /**
     * mybatisGenerator configuration
     */
    private Configuration configuration;

    public MybatisGenerator(Configuration configuration){
        this.configuration = configuration;
    }

    public static MybatisGenerator bulid(Configuration configuration){
        return new MybatisGenerator(configuration);
    }

    public MybatisGenerator templateConfiguration(freemarker.template.Configuration templateConfiguration){
        this.templateConfiguration = templateConfiguration;
        return this;
    }

    public void generator(){
        Connection connection = new JdbcConnectionBuilder(configuration.getJdbc()).getConnection();
        try {
            List<Table> dbTables = (List<Table>)DatabaseMetaDataHelper.getTablesExtractor().setTableNamePattern("T_PM_USER").extract(connection.getMetaData());
            log.info("Table元数据 -> {}", JSON.toJSONString(dbTables));
            Map<String, TableConfig> tableConfigMap = configuration.getTableConfigMap();
            List<Table> filterTables = (List)dbTables.stream().filter(dbTable -> {
                return tableConfigMap.containsKey(dbTable.getTableName());
            }).collect(Collectors.toList());
            log.info("Filter后Table元数据 -> {}", JSON.toJSONString(dbTables));
            List<DataModel> dataModels = filterTables.stream().map(dbTable -> {
                TableConfig tableConfig = tableConfigMap.get(dbTable.getTableName());
                DataModel dataModel = new DataModel();
                if(Objects.isNull(tableConfig.getModelName())){
                    String tableName = tableConfig.getTableName().toLowerCase();
                    if(tableName.contains("_")){
                        tableConfig.setModelName(camelCaseName(tableName));
                    }
                    tableConfig.setModelName(tableConfig.getTableName().substring(0, 1).toUpperCase() + tableConfig.getTableName().substring(1));
                }
                dataModel.setModelName(tableConfig.getModelName());
                dataModel.setTableName(tableConfig.getTableName());

                if(Objects.nonNull(configuration.getRepositoryFile())){
                    String repositoryName = tableConfig.getRepositoryName();
                    if(Objects.isNull(repositoryName)){
                        repositoryName = configuration.getRepositoryFile().getPrefix() + dataModel.getModelName() + configuration.getRepositoryFile().getSuffix();
                    }
                    dataModel.setRepositoryName(repositoryName);
                    dataModel.setRepositoryPackage(configuration.getRepositoryFile().getPackageName());
                }

                if(Objects.nonNull(configuration.getServiceFile())){
                    String serviceName = tableConfig.getServiceName();
                    if(Objects.isNull(serviceName)){
                        serviceName = configuration.getServiceFile().getPrefix() + dataModel.getModelName() + configuration.getServiceFile().getSuffix();
                    }
                    dataModel.setServiceName(serviceName);
                    dataModel.setServicePackage(configuration.getServiceFile().getPackageName());
                }

                if(Objects.nonNull(configuration.getRepositorySqlFile())){
                    String repositoryName = tableConfig.getRepositorySqlName();
                    if(Objects.isNull(repositoryName)){
                        repositoryName = configuration.getRepositorySqlFile().getPrefix() + dataModel.getModelName() + configuration.getRepositorySqlFile().getSuffix();
                    }
                    dataModel.setRepositorySqlName(repositoryName);
                }

                if(Objects.nonNull(configuration.getRepositoryCustomSqlFile())){
                    String customRepositoryName = tableConfig.getCustomRepositorySqlName();
                    if(Objects.isNull(customRepositoryName)){
                        customRepositoryName = configuration.getRepositoryCustomSqlFile().getPrefix() + dataModel.getModelName() + configuration.getRepositoryCustomSqlFile().getSuffix();
                    }
                    dataModel.setCustomRepositorySqlName(customRepositoryName);
                }

                dataModel.setColumns(dbTable.getColumns().stream().map(dbColumn -> {
                    DataModel.Column column = new DataModel.Column();
                    column.setName(dbColumn.getColumnName());
                    column.setProperty(camelCaseName(column.getName().toLowerCase()));
                    column.setRemarks("");
                    setJdbcTypeAndJavaType(column, dbColumn);
                    return column;
                }).collect(Collectors.toList()));

                List<PrimaryKey> dbPrimaryKeys = dbTable.getPrimaryKeys();
                if(Objects.nonNull(dbPrimaryKeys) && !dbPrimaryKeys.isEmpty()){
                    dataModel.getColumns().forEach(column -> {
                        if(column.getName().equals(dbPrimaryKeys.get(0).getColumnName())){
                            dataModel.setPrimaryKey(column);
                            return;
                        }
                    });
                }
                dataModel.setModelPackage(configuration.getModelFile().getPackageName());
                log.info("TABLE [ {} ] 主键 -> {}", dataModel.getTableName(), dataModel.getPrimaryKey().getName());
                generatorAll(dataModel);
                return dataModel;
            }).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generatorAll(DataModel dataModel){
        generatorModel(dataModel);
        generatorRepository(dataModel);
        generatorService(dataModel);
        generatorRepositorySql(dataModel);
        generatorRepositoryCustomSql(dataModel);
    }

    public void generatorModel(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getModelFile(), dataModel.getModelName());
    }

    public void generatorRepositorySql(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getRepositorySqlFile(), dataModel.getRepositorySqlName());
    }

    public void generatorRepositoryCustomSql(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getRepositoryCustomSqlFile(), dataModel.getCustomRepositorySqlName());
    }

    public void generatorRepository(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getRepositoryFile(), dataModel.getRepositoryName());
    }

    public void generatorService(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getServiceFile(), dataModel.getServiceName());
    }

    public void generatorFile(DataModel dataModel, GenerateFile generateFile, String fileName){
        String fullFileName = fileName + generateFile.getExtension();
        log.info("Generator File -> {} start ", fullFileName);
        File output = new File(generateFile.getOutputDirectory(), fullFileName);
        CodeGenerator.create(this.templateConfiguration).generator(dataModel, generateFile.getTemplate(), output);
        log.info("Generator File -> {} over ", fullFileName);
    }



    /**
     * 转换为驼峰
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }


    public static void setJdbcTypeAndJavaType(DataModel.Column column, Column dbColumn) {
        String typeName = dbColumn.getTypeName();
        if (Objects.equals(typeName, "BIGINT")) {
            column.setJdbcType(JdbcType.BIGINT.name());
            column.setJavaType(Long.class);
        } else if (Arrays.asList("INT", "INTEGER").contains(typeName)) {
            column.setJdbcType(JdbcType.INTEGER.name());
            column.setJavaType(Integer.class);
        } else if (Arrays.asList("DOUBLE", "FLOAT", "DECIMAL").contains(typeName)) {
            column.setJdbcType(JdbcType.DECIMAL.name());
            column.setJavaType(BigDecimal.class);
        } else if (Objects.equals("BIT", typeName)) {
            column.setJdbcType(JdbcType.BIT.name());
            column.setJavaType(Boolean.class);
        } else if (Objects.equals(typeName, "NUMBER")) {
            column.setJdbcType(JdbcType.DECIMAL.name());
            Integer digits = dbColumn.getDecimalDigits();
            Integer size = dbColumn.getColumnSize();
            if (digits <= 0 && size <= 18) {
                if (size >= 10) {
                    column.setJavaType(Long.class);
                } else {
                    column.setJavaType(Integer.class);
                }
            } else {
                column.setJavaType(BigDecimal.class);
            }
        } else if (!Objects.equals("DATE", typeName) && !Objects.equals("DATETIME", typeName) && !typeName.startsWith("TIMESTAMP")) {
            column.setJdbcType(JdbcType.VARCHAR.name());
            column.setJavaType(String.class);
        } else {
            column.setJdbcType(JdbcType.TIMESTAMP.name());
            column.setJavaType(Date.class);
        }
    }



}
