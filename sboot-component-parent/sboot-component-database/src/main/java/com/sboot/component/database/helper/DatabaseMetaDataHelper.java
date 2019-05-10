package com.sboot.component.database.helper;

import com.alibaba.fastjson.JSON;
import com.sboot.component.database.metadata.Column;
import com.sboot.component.database.metadata.PrimaryKey;
import com.sboot.component.database.metadata.Table;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class DatabaseMetaDataHelper {

    public static TablesExtractor getTablesExtractor(){
        return new DatabaseMetaDataHelper.TablesExtractor();
    }

    public static ColumnsExtractor getColumnsExtractor(){
        return new DatabaseMetaDataHelper.ColumnsExtractor();
    }

    public static PrimaryKeysExtractor getPrimaryKeysExtractor(){
        return new DatabaseMetaDataHelper.PrimaryKeysExtractor();
    }

    /**
     * rs结果集转换为list
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<Map> parseResultSetToList(ResultSet rs) throws SQLException {
        List<Map> list = new ArrayList<>();
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        while(rs.next()){
            Map<String, Object> map = new TreeMap<>();
            for (int i = 1; i <= columnCount; i++) {
                map.put(resultSetMetaData.getColumnLabel(i).replace("_",""), rs.getObject(i));
            }
            list.add(map);
        }
        return list;
    }


    private interface Extractor {
        Object extract(DatabaseMetaData databaseMetaData);
    }



    /**
     * 读取PrimaryKey信息
     */
    public static class PrimaryKeysExtractor implements DatabaseMetaDataHelper.Extractor {

        String catalog;
        String schema;
        String table;

        @Override
        public Object extract(DatabaseMetaData databaseMetaData) {
            try {
                ResultSet rs = databaseMetaData.getPrimaryKeys(this.catalog, this.schema, this.table);
                return JSON.parseArray(JSON.toJSONString(DatabaseMetaDataHelper.parseResultSetToList(rs)), PrimaryKey.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public String getCatalog() {
            return catalog;
        }

        public PrimaryKeysExtractor setCatalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        public String getSchema() {
            return schema;
        }

        public PrimaryKeysExtractor setSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public String getTable() {
            return table;
        }

        public PrimaryKeysExtractor setTable(String table) {
            this.table = table;
            return this;
        }
    }

    /**
     * 读取Column信息
     */
    public static class ColumnsExtractor implements DatabaseMetaDataHelper.Extractor {

        String catalog;
        String schemaPattern;
        String tableNamePattern;
        String columnNamePattern;

        @Override
        public Object extract(DatabaseMetaData databaseMetaData) {
            try {
                ResultSet rs = databaseMetaData.getColumns(this.catalog, this.schemaPattern, this.tableNamePattern, this.columnNamePattern == null ? "%" : this.columnNamePattern);
                return JSON.parseArray(JSON.toJSONString(DatabaseMetaDataHelper.parseResultSetToList(rs)), Column.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public String getCatalog() {
            return catalog;
        }

        public ColumnsExtractor setCatalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        public String getSchemaPattern() {
            return schemaPattern;
        }

        public ColumnsExtractor setSchemaPattern(String schemaPattern) {
            this.schemaPattern = schemaPattern;
            return this;
        }

        public String getTableNamePattern() {
            return tableNamePattern;
        }

        public ColumnsExtractor setTableNamePattern(String tableNamePattern) {
            this.tableNamePattern = tableNamePattern;
            return this;
        }

        public String getColumnNamePattern() {
            return columnNamePattern;
        }

        public ColumnsExtractor setColumnNamePattern(String columnNamePattern) {
            this.columnNamePattern = columnNamePattern;
            return this;
        }
    }

    /**
     * 读取Table信息
     */
    public static class TablesExtractor implements DatabaseMetaDataHelper.Extractor {
        /**
         * catalog - 表所在的类别名称;""表示获取没有类别的列,null表示获取所有类别的列。
         * schema - 表所在的模式名称(oracle中对应于Tablespace);""表示获取没有模式的列,null标识获取所有模式的列;
         * 可包含单字符通配符("_"),或多字符通配符("%");
         * tableNamePattern - 表名称;可包含单字符通配符("_"),或多字符通配符("%");
         * types - 表类型数组; "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM";
         * null表示包含所有的表类型;可包含单字符通配符("_"),或多字符通配符("%");
         */
        String catalog;
        String schemaPattern;
        String tableNamePattern;
        String[] types;

        @Override
        public Object extract(DatabaseMetaData databaseMetaData) {
            try {
                // 获取table信息
                ResultSet rs = databaseMetaData.getTables(this.catalog, this.schemaPattern, this.tableNamePattern == null ? "%" : this.tableNamePattern, this.types == null || this.types.length == 0 ? new String[]{"TABLE"} : this.types);
                List<Table> tables = JSON.parseArray(JSON.toJSONString(DatabaseMetaDataHelper.parseResultSetToList(rs)), Table.class);
                tables.forEach(table -> {
                    // 获取table -> columns 信息
                    List<Column> columns = (List<Column>)DatabaseMetaDataHelper.getColumnsExtractor().setCatalog(this.catalog).setSchemaPattern(this.schemaPattern).setTableNamePattern(table.getTableName()).extract(databaseMetaData);
                    table.setColumns(columns);
                    // 获取table -> columns 信息
                    List<PrimaryKey> primaryKeys = (List<PrimaryKey>)DatabaseMetaDataHelper.getPrimaryKeysExtractor().setCatalog(this.catalog).setSchema(table.getTableSchem()).setTable(table.getTableName()).extract(databaseMetaData);
                    table.setPrimaryKeys(primaryKeys);
                });
                return tables;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public String getCatalog() {
            return catalog;
        }

        public TablesExtractor setCatalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        public String getSchemaPattern() {
            return schemaPattern;
        }

        public TablesExtractor setSchemaPattern(String schemaPattern) {
            this.schemaPattern = schemaPattern;
            return this;
        }

        public String getTableNamePattern() {
            return tableNamePattern;
        }

        public TablesExtractor setTableNamePattern(String tableNamePattern) {
            this.tableNamePattern = tableNamePattern;
            return this;
        }

        public String[] getTypes() {
            return types;
        }

        public TablesExtractor setTypes(String[] types) {
            this.types = types;
            return this;
        }
    }

}
