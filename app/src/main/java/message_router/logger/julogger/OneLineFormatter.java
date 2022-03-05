package message_router.logger.julogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class OneLineFormatter extends Formatter {

    private String getAlternateNamesForLogLevels(String julLogLevel) {
        if (julLogLevel == "SEVERE") {
            return "ERROR";
        } else if (julLogLevel == "FINE") {
            return "DEBUG";
        } else {
            return julLogLevel;
        }
    }

    @Override
    public String format(LogRecord record) {
        SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(record.getMillis());
        String time = logTime.format(cal.getTime());
        String logLevel = getAlternateNamesForLogLevels(record.getLevel().getName());
        return time + " " + logLevel + ": " + record.getMessage() + "\n";
    }

}
