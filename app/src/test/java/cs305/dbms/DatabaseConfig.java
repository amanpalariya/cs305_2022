package cs305.dbms;

public class DatabaseConfig {
    public String database;
    public String username;
    public String password;

    public DatabaseConfig(String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public static DatabaseConfig getDefault() {
        // Please setup the MySQL database and provide the username and password
        return new DatabaseConfig("sakila", "", "");
    }
}
