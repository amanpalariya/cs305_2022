package message_router.dbms;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class SqlDatabaseReaderTest {
    String invalidUrl = "invalid-url";
    String validUrl = "jdbc:sqlite:src/test/resources/course.db";
    @Test
    void invalidUrl(){
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new SqlDatabaseReader(invalidUrl);
            }
        });
    }

    @Test
    void validUrl(){
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new SqlDatabaseReader(validUrl);
            }
        });
    }

    @Test
    void executeQueryAndUpdate() throws SQLException{
        SqlDatabaseReader databaseReader = new SqlDatabaseReader(validUrl);
        ResultSet rs = databaseReader.executeQuery("SELECT name FROM course WHERE id IS \"CS305\"");
        rs.next();
        assertEquals(rs.getString("name"), "Software Engineering");
        int result = databaseReader.executeUpdate("DELETE FROM course WHERE id IS \"HS305\"");
        assertEquals(result, 0);
    }
}
