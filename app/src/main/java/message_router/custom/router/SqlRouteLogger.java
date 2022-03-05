package message_router.custom.router;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import message_router.config.Config;
import message_router.core.router.RouteLog;
import message_router.core.router.RouteLogger;
import message_router.dbms.SqlDatabaseReader;

public class SqlRouteLogger implements RouteLogger {
    private SqlDatabaseReader databaseReader;
    private String tableName;
    private final String ROUTE_ID_COLUMN = "RouteId";
    private final String EVENT_TYPE_COLUMN = "EventType";
    private final String EVENT_TIME_COLUMN = "EventTime";

    public SqlRouteLogger(SqlDatabaseReader databaseReader, String tableName){
        this.databaseReader = databaseReader;
        this.tableName = tableName;
    }

    private String formatDateForSql(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");
        return format.format(date);
    }

    private String getInsertQuery(RouteLog log) {
        return "INSERT INTO " + tableName + "(" + ROUTE_ID_COLUMN + ", " + EVENT_TYPE_COLUMN + ", " + EVENT_TIME_COLUMN
                + ") VALUES(" + log.getRouteId() + ", \"" + log.getEventType() + "\", \""
                + formatDateForSql(log.getEventTime()) + "\")";
    }

    @Override
    public void writeLog(RouteLog log) {
        try {
            databaseReader.executeUpdate(getInsertQuery(log));
        } catch (SQLException e) {
            Config.getLogger().warning("Unable to write log to DB, error: " + e.getMessage());
        }
    }

}
