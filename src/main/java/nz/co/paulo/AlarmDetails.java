package nz.co.paulo;

import spark.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by martin paulo on 11/08/2014.
 */
public class AlarmDetails {

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    String alarmId;
    String previous;
    String current;
    String reason;
    String reasonData;
    String timeReported;

    public AlarmDetails(Request request) {
        Date today = Calendar.getInstance().getTime();
        timeReported = df.format(today);
        alarmId = getNonNullValue(request.queryParams("alarm_id"));
        previous = getNonNullValue(request.queryParams("previous"));
        current = getNonNullValue(request.queryParams("current"));
        reason = getNonNullValue(request.queryParams("reason"));
        reasonData = getNonNullValue(request.queryParams("reason_data"));
    }

    private String getNonNullValue(String value) {
        return value == null ? " - " : value;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public String getPrevious() {
        return previous;
    }

    public String getCurrent() {
        return current;
    }

    public String getReason() {
        return reason;
    }

    public String getReasonData() {
        return reasonData;
    }

    public String getTimeReported() {
        return timeReported;
    }

    @Override
    public String toString() {
        return "Id: " + alarmId + " Time: " + getTimeReported();
    }
}
