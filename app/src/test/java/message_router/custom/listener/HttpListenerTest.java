package message_router.custom.listener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class HttpListenerTest {
    @Test
    void listenerTest(){
        assertDoesNotThrow(
            new Executable() {
                @Override
                public void execute() throws Throwable {
                    HttpListener listener = new HttpListener(8100);
                    listener.listen();
                }
            }
        );
    }
}
