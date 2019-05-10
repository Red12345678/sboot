package com.sboot.component.database;

/**
 * @author tuozq
 * @description: 数据库驱动类
 * @date 2019/5/10.
 */
public enum JdbcDriverEnum {

    ORACLE_DRIVER("ORACLE_DRIVER", "oracle.jdbc.driver.OracleDriver")
    ,SQLSERVER_DRIVER("ORACLE_DRIVER", "com.microsoft.jdbc.sqlserver.SQLServerDriver")
    ,MYSQL_DRIVER("ORACLE_DRIVER", "com.mysql.jdbc.Driver");


    private String value;

    private String alias;

    JdbcDriverEnum(String alias, String value) {
        this.alias = alias;
        this.value = value;
    }

    public String value(){
        return this.value;
    }

    public String alias(){
        return this.alias;
    }
}
