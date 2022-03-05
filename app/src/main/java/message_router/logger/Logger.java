package message_router.logger;

public interface Logger {
    public void muteAll();
    public void unmuteAll();

    public void info(String message);
    public void muteInfo();
    public void unmuteInfo();

    public void warning(String message);
    public void muteWarning();
    public void unmuteWarning();

    public void error(String message);
    public void muteError();
    public void unmuteError();

    public void debug(String message);
    public void muteDebug();
    public void unmuteDebug();
}
