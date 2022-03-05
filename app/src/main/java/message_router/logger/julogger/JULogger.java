package message_router.logger.julogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;

import message_router.logger.Logger;

public class JULogger implements Logger {
    private boolean isInfoMuted, isWarningMuted, isErrorMuted, isDebugMuted;
    private java.util.logging.Logger logger;

    public JULogger(String filepath) throws SecurityException, IOException {
        Formatter customFormatter = new OneLineFormatter();

        FileHandler fileHandler = new FileHandler(filepath);
        fileHandler.setFormatter(customFormatter);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(customFormatter);

        logger = java.util.logging.Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);
        logger.addHandler(consoleHandler);

        logger.setLevel(Level.ALL);
        unmuteAll();
    }

    @Override
    public void muteAll() {
        muteInfo();
        muteWarning();
        muteError();
        muteDebug();
    }

    @Override
    public void unmuteAll() {
        unmuteInfo();
        unmuteWarning();
        unmuteError();
        unmuteDebug();
    }

    @Override
    public void info(String message) {
        if (!isInfoMuted)
            logger.info(message);
    }

    @Override
    public void muteInfo() {
        isInfoMuted = true;
    }

    @Override
    public void unmuteInfo() {
        isInfoMuted = false;
    }

    @Override
    public void warning(String message) {
        if (!isWarningMuted)
            logger.warning(message);
    }

    @Override
    public void muteWarning() {
        isWarningMuted = true;
    }

    @Override
    public void unmuteWarning() {
        isWarningMuted = false;
    }

    @Override
    public void error(String message) {
        if (!isErrorMuted)
            logger.severe(message);
    }

    @Override
    public void muteError() {
        isErrorMuted = true;
    }

    @Override
    public void unmuteError() {
        isErrorMuted = false;
    }

    @Override
    public void debug(String message) {
        if (!isDebugMuted)
            logger.fine(message);
    }

    @Override
    public void muteDebug() {
        isDebugMuted = true;
    }

    @Override
    public void unmuteDebug() {
        isDebugMuted = false;
    }
}