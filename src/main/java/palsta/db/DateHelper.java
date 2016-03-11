package palsta.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateHelper {

    public static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat dateFormat = new SimpleDateFormat(dateTimePattern);

    private boolean useTimestampType;

    public DateHelper(boolean useTimestampType) {
        this.useTimestampType = useTimestampType;
    }

    public Timestamp getTimestamp(ResultSet rs, String columnLabel) throws SQLException {
        if (rs == null) {
            return null;
        }

        if (useTimestampType) {
            return rs.getTimestamp(columnLabel);
        }

        String timeString = rs.getString(columnLabel);
        if (timeString == null) {
            return null;
        }

        return Timestamp.valueOf(timeString);
    }

    public Object saveAs(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        if (useTimestampType) {
            return timestamp;
        }

        return dateFormat.format(timestamp);
    }

    public String toString(Timestamp timestamp) {
        return dateFormat.format(timestamp);
    }

}
