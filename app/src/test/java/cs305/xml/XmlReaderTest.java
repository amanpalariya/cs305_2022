package cs305.xml;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class XmlReaderTest {
    String filepath = "src/test/resources/queries.xml";
    String filepathOfIllFormedXml = "src/test/resources/ill-formed.xml";
    String queryId = "findMovies";
    String queryString = "SELECT a, b, c FROM my_table WHERE x=${propX} AND y=${propY};";
    String paramType = "org.foo.Bar";

    @Test
    void fileNotFoundTest() {
        assertThrows(IOException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new XmlReader("path-that-does-not-exist");
            }
        });
    }

    @Test
    void parsingUnsuccessful() {
        assertThrows(XmlParsingException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new XmlReader(filepathOfIllFormedXml);
            }
        });
    }

    @Test
    void parsingSuccessful() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new XmlReader(filepath);
            }
        });
    }

    @Test
    void tagNotFound() throws IOException, XmlParsingException {
        XmlReader xmlReader = new XmlReader(filepath);
        assertThrows(NoSqlTagWithGivenIdException.class, new Executable() {
            public void execute() throws Throwable {
                xmlReader.getTagWithId("query-that-does-not-exist");
            };
        });
        assertThrows(NoSqlTagWithGivenIdException.class, new Executable() {
            public void execute() throws Throwable {
                xmlReader.getTagWithId("illegal\"; id");
            };
        });
    }

    @Test
    void tagFound() throws IOException, XmlParsingException {
        XmlReader xmlReader = new XmlReader(filepath);
        assertDoesNotThrow(new Executable() {
            public void execute() throws Throwable {
                xmlReader.getTagWithId(queryId);
            };
        });
    }

    @Test
    void tagMatches() throws IOException, XmlParsingException, NoSqlTagWithGivenIdException {
        XmlReader xmlReader = new XmlReader(filepath);
        SqlTag sqlTag = xmlReader.getTagWithId(queryId);
        assertEquals(sqlTag.getQuery(), queryString);
        assertEquals(sqlTag.getParamType(), paramType);
    }
}
