package com.sboot.component.database.metadata;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class PrimaryKey {

    private String columnName;
    private Integer keySeq;
    private String pkName;
    private String tableName;
    private String tableSchem;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(Integer keySeq) {
        this.keySeq = keySeq;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

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
}
