package message_router.custom.router;

import java.sql.ResultSet;
import java.sql.SQLException;

import message_router.core.router.Route;
import message_router.core.router.RouteFinder;
import message_router.dbms.SqlDatabaseReader;

public class SqlRouteFinder implements RouteFinder {
    private SqlDatabaseReader databaseReader;
    private String tableName;
    private final String ROUTE_ID_COLUMN = "RouteId";
    private final String SENDER_COLUMN = "Sender";
    private final String MESSAGE_TYPE_COLUMN = "MessageType";
    private final String DESTINATION_COLUMN = "Destination";

    public SqlRouteFinder(SqlDatabaseReader databaseReader, String tableName) {
        this.databaseReader = databaseReader;
        this.tableName = tableName;
    }

    private String getSearchQuery(String sender, String messageType) {
        return "SELECT " + ROUTE_ID_COLUMN + ", " + SENDER_COLUMN + ", " + MESSAGE_TYPE_COLUMN + ", "
                + DESTINATION_COLUMN + " from " + tableName + " WHERE " + SENDER_COLUMN + "=\"" + sender
                + "\" AND " + MESSAGE_TYPE_COLUMN + "=\"" + messageType + "\";";
    }

    Route getRouteFromResultSet(ResultSet rs) {
        try {
            rs.next();
            int routeId = rs.getInt(ROUTE_ID_COLUMN);
            String sender = rs.getString(SENDER_COLUMN);
            String messageType = rs.getString(MESSAGE_TYPE_COLUMN);
            String destination = rs.getString(DESTINATION_COLUMN);
            return new Route(routeId, sender, messageType, destination);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Route getRoute(String sender, String messageType) {
        try {
            ResultSet rs = databaseReader.executeQuery(getSearchQuery(sender, messageType));
            return getRouteFromResultSet(rs);
        } catch (SQLException e) {
            return null;
        }
    }

}
