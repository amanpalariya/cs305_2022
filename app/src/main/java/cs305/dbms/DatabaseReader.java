package cs305.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseReader {
    private Connection conn;
    private Statement stmt;

    public static String getConnectionUrl(String databaseName, String username,
            String password) {
        return "jdbc:mysql://localhost/" + databaseName + "?user=" + username + "&password="
                + password;
    }

    public DatabaseReader(String databaseName, String username, String password)
            throws SQLException {
        String url = DatabaseReader.getConnectionUrl(databaseName, username, password);
        this.conn = DriverManager.getConnection(url);
        this.stmt = this.conn.createStatement();
    }

    public <T> int executeUpdate(SqlQuery<T> query, T param)
            throws SQLException, IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        return stmt.executeUpdate(query.getQuery(param));
    }

    public <T> ResultSet executeQuery(SqlQuery<T> query, T param)
            throws SQLException, IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        return stmt.executeQuery(query.getQuery(param));
    }
}
