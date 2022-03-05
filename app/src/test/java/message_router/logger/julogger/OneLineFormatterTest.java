package message_router.logger.julogger;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.Test;

public class OneLineFormatterTest {

    String message = "test message";

    @Test
    void formatDebugTest(){
        Formatter formatter = new OneLineFormatter();
        String formattedMessage = formatter.format(new LogRecord(Level.FINE, message));
        assertTrue(formattedMessage.substring(20).startsWith("DEBUG"));
    }

    @Test
    void formatErrorTest(){
        Formatter formatter = new OneLineFormatter();
        String formattedMessage = formatter.format(new LogRecord(Level.SEVERE, message));
        assertTrue(formattedMessage.substring(20).startsWith("ERROR"));
    }

    @Test
    void formatOtherTest(){
        Formatter formatter = new OneLineFormatter();
        String formattedMessage = formatter.format(new LogRecord(Level.WARNING, message));
        assertTrue(formattedMessage.substring(20).startsWith("WARNING"));
    }
}
