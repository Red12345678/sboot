package com.sboot.component.database.basic.model.mybatis;

/**
 * @author tuozq
 * @description: model基类
 * @date 2019/5/10.
 */
public abstract class BaseEntity<T> {

    public abstract T getId();

    public abstract void setId(T Id);

}
