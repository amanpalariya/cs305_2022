package message_router.logger.julogger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import message_router.logger.Logger;

public class JULoggerTest {
    String filepath = "src/test/resources/logs.txt";
    String message = "test message";
    @Test
    void initializationTest() throws SecurityException, IOException{
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new JULogger(filepath);
            }
        });
    }

    @Test
    void muteUnmuteTest() throws SecurityException, IOException{
        Logger logger = new JULogger(filepath);
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                logger.muteAll();
                logger.unmuteAll();
            }
        });
    }

    @Test
    void loggingTest() throws SecurityException, IOException{
        Logger logger = new JULogger(filepath);
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                logger.info(message);
                logger.warning(message);
                logger.error(message);
                logger.debug(message);
            }
        });
    }

    @Test
    void mutedLoggingTest() throws SecurityException, IOException{
        Logger logger = new JULogger(filepath);
        logger.muteAll();
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                logger.info(message);
                logger.warning(message);
                logger.error(message);
                logger.debug(message);
            }
        });
    }
}
