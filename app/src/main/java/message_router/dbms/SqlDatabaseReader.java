package message_router.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDatabaseReader {
    private Connection conn;
    private Statement stmt;

    public SqlDatabaseReader(String url) throws SQLException{
        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();
    }

    public ResultSet executeQuery(String query) throws SQLException{
        return stmt.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException{
        return stmt.executeUpdate(query);
    }

    @Override
    protected void finalize() throws SQLException{
        stmt.close();
        conn.close();
    }
}
