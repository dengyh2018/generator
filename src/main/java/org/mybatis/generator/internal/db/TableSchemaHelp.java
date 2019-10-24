package org.mybatis.generator.internal.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.JDBC4Connection;
import com.mysql.jdbc.JDBC4DatabaseMetaDataUsingInfoSchema;

public class TableSchemaHelp {

    private Connection connection;

    private DatabaseMetaData databaseMetaData;

    public TableSchemaHelp(Connection connection) {
        super();
        this.connection = connection;
        if (connection instanceof JDBC4Connection) {
            try {
                databaseMetaData = new JDBC4DatabaseMetaDataUsingInfoSchema((JDBC4Connection) connection, connection.getCatalog());
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            String catalog = connection.getCatalog();
            System.out.println(catalog);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String getTableRemark(String tableName) {
        String remarks = null;
        try {
            String catalog = connection.getCatalog();
            ResultSet resultSet = databaseMetaData.getTables(null, null, tableName, null);
            if (resultSet.next()) {
                remarks = resultSet.getString("REMARKS");
            }
        }
        catch (Exception e) {
           e.printStackTrace();
        }
        return remarks;
    }

}
