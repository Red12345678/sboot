package com.sboot.component.mybatis.generator;

import com.sboot.component.database.Jdbc;
import com.sboot.component.database.JdbcDriverEnum;
import com.sboot.component.mybatis.generator.configuration.Configuration;
import com.sboot.component.mybatis.generator.configuration.GenerateJavaFile;
import com.sboot.component.mybatis.generator.configuration.GenerateXmlFile;
import com.sboot.component.mybatis.generator.configuration.TableConfig;

import java.io.File;
import java.util.*;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class Main {

    static Configuration configuration = new Configuration();

    public static void main(String[] args){
        String baseFolder = "F:\\code-generator";
        // freemarker 模板配置
        freemarker.template.Configuration templateConfiguration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
        templateConfiguration.setClassForTemplateLoading(Main.class, "/generator/template/");
        // 代码生成配置信息
        configuration.setJdbc(new Jdbc().url("jdbc:oracle:thin:@222.79.247.164:11521/atrac").driver("oracle.jdbc.driver.OracleDriver").user("TEST170314").password("kingdee"));
        GenerateJavaFile modelFile = new GenerateJavaFile();
        modelFile.setOutputDirectory(new File(baseFolder + "/java/com/scloud/app/cdm/datasource/cdm/model"));
        modelFile.setPackageName("com.sboot.component.mybatis.generator.test.model");
        modelFile.setTemplate("model.ftl");
        configuration.setModelFile(modelFile);
        // 数据层
        GenerateJavaFile repositoryFile = new GenerateJavaFile();
        repositoryFile.setOutputDirectory(new File(baseFolder + "/java/com/scloud/app/cdm/datasource/cdm/repository"));
        repositoryFile.setPackageName("com.sboot.component.mybatis.generator.test.repository");
        repositoryFile.setTemplate("repository.ftl");
        repositoryFile.setSuffix("Repository");
        repositoryFile.setPrefix("");
        configuration.setRepositoryFile(repositoryFile);
        // 业务层
        GenerateJavaFile serviceFile = new GenerateJavaFile();
        serviceFile.setOutputDirectory(new File(baseFolder + "/java/com/scloud/app/cdm/datasource/cdm/service"));
        serviceFile.setPackageName("com.sboot.component.mybatis.generator.test.service");
        serviceFile.setTemplate("service.ftl");
        serviceFile.setSuffix("Service");
        serviceFile.setPrefix("");
        configuration.setServiceFile(serviceFile);
        // 数据层sql
        GenerateXmlFile repositorySqlFile = new GenerateXmlFile();
        repositorySqlFile.setOutputDirectory(new File(baseFolder + "/resource/com/scloud/app/cdm/datasource/cdm/repository/auto"));
        if(configuration.getJdbc().getDriver().equals(JdbcDriverEnum.ORACLE_DRIVER.value())){
            repositorySqlFile.setTemplate("repositorySqlForOracle.ftl");
        }else if(configuration.getJdbc().getDriver().equals(JdbcDriverEnum.MYSQL_DRIVER.value())){
            repositorySqlFile.setTemplate("repositorySqlForMysql.ftl");
        }
        repositorySqlFile.setSuffix("Mapper");
        repositorySqlFile.setPrefix("_");
        configuration.setRepositorySqlFile(repositorySqlFile);
        // 数据层sql 自定义修改
        GenerateXmlFile repositoryCustomSqlFile = new GenerateXmlFile();
        repositoryCustomSqlFile.setOutputDirectory(new File(baseFolder + "/resource/com/scloud/app/cdm/datasource/cdm/repository"));
        repositoryCustomSqlFile.setTemplate("repositorySqlForCustom.ftl");
        repositoryCustomSqlFile.setSuffix("Mapper");
        repositoryCustomSqlFile.setPrefix("");
        configuration.setRepositoryCustomSqlFile(repositoryCustomSqlFile);

        // 针对每个table配置，可定义model、service、repository
        TableConfig tableConfig = new TableConfig();
        tableConfig.setModelName("User");
        tableConfig.setTableName("T_PM_USER");
        configuration.setTableConfigs(Arrays.asList(tableConfig));
        MybatisGenerator.bulid(configuration).templateConfiguration(templateConfiguration).generator();


    }





}
