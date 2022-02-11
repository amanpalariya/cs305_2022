package cs305.sqlrunner;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class InvalidReturnTypeExceptionTest {
    @Test
    void exceptionTest() {
        assertThrows(InvalidReturnTypeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                throw new InvalidReturnTypeException(Integer.class);
            }
        });
    }
}
