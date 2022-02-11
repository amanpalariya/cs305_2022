package cs305;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs305.dbms.IllFormedParamException;
import cs305.dbms.PrimitiveNotImplementedException;
import cs305.sqlrunner.InconsistentParamTypeException;
import cs305.sqlrunner.InvalidReturnTypeException;
import cs305.sqlrunner.MySqlRunner;
import cs305.xml.NoSqlTagWithGivenIdException;
import cs305.xml.XmlParsingException;

public class App {
    public static void main(String[] args) {
        MySqlRunner sqlRunner;
        try {
            sqlRunner = new MySqlRunner("sakila", "", "", "src/main/resources/queries.xml");

            List<Integer> filmIds = new ArrayList<>();
            filmIds.add(2);
            filmIds.add(19);
            filmIds.add(153);
            List<Film> films = sqlRunner.selectMany("getMovies", filmIds, Film.class);
            for (Film film : films) {
                System.out.println(film);
            }
        } catch (SQLException | IOException | XmlParsingException | IllegalArgumentException
                | InvalidReturnTypeException | NoSqlTagWithGivenIdException | IllFormedParamException
                | InconsistentParamTypeException | PrimitiveNotImplementedException e) {
            e.printStackTrace();
        }
    }
}
