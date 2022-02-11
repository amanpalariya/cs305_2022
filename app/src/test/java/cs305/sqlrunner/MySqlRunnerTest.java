package cs305.sqlrunner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import cs305.dbms.DatabaseConfig;
import cs305.dbms.IllFormedParamException;
import cs305.dbms.PrimitiveNotImplementedException;
import cs305.xml.NoSqlTagWithGivenIdException;
import cs305.xml.XmlParsingException;

public class MySqlRunnerTest {
    String filepath = "src/test/resources/queries.xml";
    String filepathOfIllFormedXml = "src/test/resources/ill-formed.xml";
    String queryId = "findMovies";
    String queryString = "SELECT a, b, c FROM my_table WHERE x=${propX} AND y=${propY};";
    String paramType = "org.foo.Bar";
    DatabaseConfig dbConfig = DatabaseConfig.getDefault();

    @Test
    void cannotFindFile() {
        assertThrows(IOException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new MySqlRunner("wrong-database", "wrong-username", "wrong-password", "path-that-does-not-exist.xml");
            }
        });
    }

    @Test
    void illFormedXml() {
        assertThrows(XmlParsingException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new MySqlRunner("wrong-database", "wrong-username", "wrong-password",
                        filepathOfIllFormedXml);
            }
        });
    }

    @Test
    void cannotConnect() {
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new MySqlRunner("wrong-database", "wrong-username", "wrong-password", filepath);
            }
        });
    }

    @Test
    void connects() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new MySqlRunner(dbConfig.database, dbConfig.username, dbConfig.password, filepath);
            }
        });
    }

    @Test
    void paramMismatchInXmlAndSql() {
        assertThrows(InconsistentParamTypeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                MySqlRunner sqlRunner = new MySqlRunner(dbConfig.database,
                        dbConfig.username,
                        dbConfig.password, filepath);
                sqlRunner.selectOne("getMovie", "id", Film.class);
            }
        });
    }

    @Test
    void invalidReturnType() {
        assertThrows(InvalidReturnTypeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                MySqlRunner sqlRunner = new MySqlRunner(dbConfig.database,
                        dbConfig.username,
                        dbConfig.password, filepath);
                sqlRunner.selectOne("getMovie", 12, Actor.class);
            }
        });
    }

    @Test
    void selectOne() throws IllegalArgumentException, InconsistentParamTypeException, NoSqlTagWithGivenIdException,
            InvalidReturnTypeException, SQLException, IllFormedParamException, IOException, XmlParsingException,
            PrimitiveNotImplementedException {
        MySqlRunner sqlRunner = new MySqlRunner(dbConfig.database,
                dbConfig.username,
                dbConfig.password, filepath);
        Film film = sqlRunner.selectOne("getMovie", 12, Film.class);
        assertEquals(film.title, "ALASKA PHANTOM");
        assertEquals(film.length, 136);
    }

    @Test
    void selectMany() throws IllegalArgumentException, InconsistentParamTypeException, NoSqlTagWithGivenIdException,
            InvalidReturnTypeException, SQLException, IllFormedParamException, IOException, XmlParsingException,
            PrimitiveNotImplementedException {
        MySqlRunner sqlRunner = new MySqlRunner(dbConfig.database,
                dbConfig.username,
                dbConfig.password, filepath);
        List<Integer> filmIds = new ArrayList<>();
        filmIds.add(2);
        filmIds.add(19);
        filmIds.add(153);
        List<Film> films = sqlRunner.selectMany("getMovies", filmIds, Film.class);
        assertEquals(films.get(0).title, "ACE GOLDFINGER");
        assertEquals(films.get(1).title, "AMADEUS HOLY");
        assertEquals(films.get(2).title, "CITIZEN SHREK");
        assertEquals(films.get(0).length, 48);
        assertEquals(films.get(1).length, 113);
        assertEquals(films.get(2).length, 165);
    }

    @Test
    void update() throws IllegalArgumentException, InconsistentParamTypeException, NoSqlTagWithGivenIdException,
            InvalidReturnTypeException, SQLException, IllFormedParamException, IOException, XmlParsingException,
            PrimitiveNotImplementedException {
        MySqlRunner sqlRunner = new MySqlRunner(dbConfig.database,
                dbConfig.username,
                dbConfig.password, filepath);
        int result = sqlRunner.update("updateMovie", -10);
        assertEquals(result, 0);
    }

    public class Actor {
        public String firstName;
        public String lastName;

        public Actor(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    @Test
    void insert() throws IllegalArgumentException, InconsistentParamTypeException, NoSqlTagWithGivenIdException,
            InvalidReturnTypeException, SQLException, IllFormedParamException, IOException, XmlParsingException,
            PrimitiveNotImplementedException {
        MySqlRunner sqlRunner = new MySqlRunner(dbConfig.database,
                dbConfig.username,
                dbConfig.password, filepath);
        int result = sqlRunner.insert("insertActor", new Actor("First", "Last"));
        assertEquals(result, 1);
    }

    @Test
    void delete() throws IllegalArgumentException, InconsistentParamTypeException, NoSqlTagWithGivenIdException,
            InvalidReturnTypeException, SQLException, IllFormedParamException, IOException, XmlParsingException,
            PrimitiveNotImplementedException {
        MySqlRunner sqlRunner = new MySqlRunner(dbConfig.database,
                dbConfig.username,
                dbConfig.password, filepath);
        int result = sqlRunner.delete("deleteActor", -10);
        assertEquals(result, 0);
    }
}
