package com.sboot.component.database.metadata;

import java.util.List;

/**
 * @author tuozq
 * @description: 数据库表信息
 * @date 2019/5/9.
 */
public class Table {

    private String tableName;

    private String tableSchem;

    private String tableType;

    private List<Column> columns;

    private List<PrimaryKey> primaryKeys;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<PrimaryKey> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<PrimaryKey> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}
