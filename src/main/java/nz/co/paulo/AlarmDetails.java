package nz.co.paulo;

import com.google.gson.Gson;
import spark.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Martin Paulo on 11/08/2014.
 * A holder for the details passed in a Ceilometer alarm call
 */
public class AlarmDetails {

    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // names must match the json names...
    String alarm_id;
    String previous;
    String current;
    String reason;
    transient String reason_data;
    transient String timeReported;

    public AlarmDetails(Request request) {
        timeReported = df.format(Calendar.getInstance().getTime());
        if (request.queryParams().size() > 0) {
            // came in from the form
            alarm_id = getNonNullValue(request.queryParams("alarm_id"));
            previous = getNonNullValue(request.queryParams("previous"));
            current = getNonNullValue(request.queryParams("current"));
            reason = getNonNullValue(request.queryParams("reason"));
            reason_data = getNonNullValue(request.queryParams("reason_data"));
        } else {
            System.out.println(request.body());
            Gson gson = new Gson();
            AlarmDetails alarmDetails = gson.fromJson(request.body(), AlarmDetails.class);
            alarm_id = alarmDetails.alarm_id;
            previous = alarmDetails.previous;
            current = alarmDetails.current;
            reason = alarmDetails.reason;
            reason_data = alarmDetails.reason_data;
        }
    }

    private String getNonNullValue(String value) {
        return value == null ? " - " : value;
    }

    public String getAlarmId() {
        return alarm_id;
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

    public String getReason_data() {
        return reason_data;
    }

    public String getTimeReported() {
        return timeReported;
    }

    @Override
    public String toString() {
        return "Id: " + alarm_id + " Time: " + getTimeReported();
    }
}
