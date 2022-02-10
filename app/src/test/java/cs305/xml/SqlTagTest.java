package cs305.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SqlTagTest {
    @Test
    void constructorTest() {
        String paramType = "some.random.type";
        String queryString = "SELECT * FROM data;";
        SqlTag tag = new SqlTag(queryString, paramType);

        assertEquals(tag.getQuery(), queryString);
        assertEquals(tag.getParamType(), paramType);
    }
}
