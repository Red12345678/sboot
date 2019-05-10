package com.sboot.component.database.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class JdbcExecuteHelper {

    public static ResultSet executeQuery(Connection connection, String sql){
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(st != null){
                    st.close();
                }
                if(rs != null){
                    rs.close();
                }
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

}
