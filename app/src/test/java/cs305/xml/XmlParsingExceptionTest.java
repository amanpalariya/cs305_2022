package cs305.xml;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class XmlParsingExceptionTest {
    @Test
    void exceptionTest() {
        assertThrows(XmlParsingException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                throw new XmlParsingException();
            }
        });
    }
}
