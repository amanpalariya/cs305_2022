package message_router.config;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import message_router.logger.Logger;
import message_router.logger.julogger.JULogger;

public class Config {
    private static String dbUrl;
    private static int port;
    private static String host;
    private static Logger logger;
    private static String routeTable;
    private static String logTable;
    private static boolean initialized = false;

    private static String HOST_KEY = "host";
    private static String PORT_KEY = "port";
    private static String DB_URL_KEY = "db_url";
    private static String LOG_FILE_KEY = "log_file";
    private static String ROUTE_TABLE_KEY = "route_table";
    private static String LOG_TABLE_KEY = "log_table";

    public static void initalize(String filepath) throws FileNotFoundException, IOException {
        JsonObject obj = (JsonObject) JsonParser.parseReader(new FileReader(filepath));
        port = obj.get(PORT_KEY).getAsInt();
        host = obj.get(HOST_KEY).getAsString();
        dbUrl = obj.get(DB_URL_KEY).getAsString();
        String logFilepath = obj.get(LOG_FILE_KEY).getAsString();
        logger = new JULogger(logFilepath);
        routeTable = obj.get(ROUTE_TABLE_KEY).getAsString();
        logTable = obj.get(LOG_TABLE_KEY).getAsString();
        initialized = true;
    }

    private static void assertInitialized(){
        assertTrue("Cannot access config without initializing", initialized);
    }

    public static String getDbUrl() {
        assertInitialized();
        return dbUrl;
    }

    public static int getPort() {
        assertInitialized();
        return port;
    }

    public static String getHost() {
        assertInitialized();
        return host;
    }

    public static Logger getLogger() {
        assertInitialized();
        return logger;
    }

    public static String getRouteTableName() {
        assertInitialized();
        return routeTable;
    }

    public static String getLogTableName() {
        assertInitialized();
        return logTable;
    }
}
