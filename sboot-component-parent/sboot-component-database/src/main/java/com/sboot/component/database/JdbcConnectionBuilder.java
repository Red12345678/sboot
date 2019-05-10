package com.sboot.component.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @author tuozq
 * @description: 数据库连接
 * @date 2019/5/9.
 */
public class JdbcConnectionBuilder {

    private Properties properties = new Properties();

    private String driver;

    private String url;

    public JdbcConnectionBuilder(String url, String driver){
        this.url = url;
        this.driver = driver;
    }

    public JdbcConnectionBuilder(Jdbc jdbc){
        this.url = jdbc.getUrl();
        this.driver = jdbc.getDriver();
        this.user(jdbc.getUser()).password(jdbc.getPassword());
    }

    public static JdbcConnectionBuilder create(String url, String driver) {
        return new JdbcConnectionBuilder(url, driver);
    }

    public JdbcConnectionBuilder user(String user) {
        properties.put("user", user);
        return this;
    }

    public JdbcConnectionBuilder password(String password) {
        properties.put("password", password);
        return this;
    }


    public Connection getConnection(){
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
