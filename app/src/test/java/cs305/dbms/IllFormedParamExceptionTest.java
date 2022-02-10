package cs305.dbms;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class IllFormedParamExceptionTest {
    @Test
    void exceptionTest() {
        assertThrows(IllFormedParamException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                throw new IllFormedParamException("field", "class");
            }
        });
    }
}
