package cs305.dbms;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class PrimitiveNotImplementedExceptionTest {
    @Test
    void exceptionTest() {
        assertThrows(PrimitiveNotImplementedException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                throw new PrimitiveNotImplementedException("some.unimplemented.Class");
            }
        });
    }
}
