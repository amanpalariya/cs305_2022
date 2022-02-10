package cs305.dbms;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class DatabaseReaderTest {
    DatabaseConfig dbConfig = DatabaseConfig.getDefault();

    @Test
    void cannotConnect() {
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new DatabaseReader("wrong-database", "wrong-username", "wrong-password");
            }
        });
    }

    @Test
    void connects() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new DatabaseReader(dbConfig.database, dbConfig.username, dbConfig.password);
            }
        });
    }

    @Test
    void runsQuery()
            throws SQLException, IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        DatabaseReader dbReader = new DatabaseReader(dbConfig.database, dbConfig.username,
                dbConfig.password);
        ResultSet rs = dbReader.executeQuery(new SqlQuery<Integer>("SELECT * FROM film WHERE film_id=${value}"), 12);
        assertTrue(rs.next());
        assertEquals(rs.getInt("film_id"), 12);
        assertEquals(rs.getString("title"), "ALASKA PHANTOM");
    }

    @Test
    void runsUpdate()
            throws SQLException, IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        DatabaseReader dbReader = new DatabaseReader(dbConfig.database, dbConfig.username,
                dbConfig.password);
        int result = dbReader.executeUpdate(new SqlQuery<String>("UPDATE film SET title=${value} WHERE film_id=-10"),
                "NEW TITLE");
        assertEquals(result, 0);
    }
}
