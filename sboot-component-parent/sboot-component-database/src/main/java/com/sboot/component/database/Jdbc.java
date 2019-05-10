package com.sboot.component.database;

/**
 * @author tuozq
 * @description: 数据库连接属性
 * @date 2019/5/9.
 */
public class Jdbc {

    private String user;

    private String password;

    private String url;

    private String driver;

    public String getUser() {
        return user;
    }

    public Jdbc user(String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Jdbc password(String password) {
        this.password = password;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Jdbc url(String url) {
        this.url = url;
        return this;
    }

    public String getDriver() {
        return driver;
    }

    public Jdbc driver(String driver) {
        this.driver = driver;
        return this;
    }
}
