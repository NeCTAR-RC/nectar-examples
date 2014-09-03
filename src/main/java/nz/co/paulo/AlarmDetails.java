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

    // So the json is something along the lines of:
    // {
    //      "current": "ok",
    //      "alarm_id": "4a4579b6-3c24-42f8-b0bc-45b12148b176",
    //      "reason": "Transition to ok due to 1 samples inside threshold, most recent: 15.5083333333",
    //      "reason_data": {
    //          "count": 1,
    //          "most_recent": 15.508333333333333,
    //          "type": "threshold",
    //          "disposition": "inside"
    //          },
    //      "previous": "alarm"
    // }

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    static class ReasonData {
        int count;
        float most_recent;
        String type;
        String disposition;

        @Override
        public String toString() {
            return "Count: " + count + "<br /> Type: " + type + "<br />  Disposition: " +
                    disposition + "<br />  Most Recent: " + most_recent;
        }
    }


    // names must match the json names...
    String alarm_id;
    String previous;
    String current;
    String reason;
    ReasonData reason_data;
    transient String timeReported;

    public AlarmDetails(Request request) {
        timeReported = DATE_FORMAT.format(Calendar.getInstance().getTime());
        if (request.queryParams().size() > 0) {
            readFromForm(request);
        } else {
            readFromBody(request);
        }
    }

    private void readFromBody(Request request) {
        AlarmDetails alarmDetails = new Gson().fromJson(request.body(), AlarmDetails.class);
        alarm_id = alarmDetails.alarm_id;
        previous = alarmDetails.previous;
        current = alarmDetails.current;
        reason = alarmDetails.reason;
        reason_data = alarmDetails.reason_data;
    }

    private void readFromForm(Request request) {
        alarm_id = getNonNullValue(request.queryParams("alarm_id"));
        previous = getNonNullValue(request.queryParams("previous"));
        current = getNonNullValue(request.queryParams("current"));
        reason = getNonNullValue(request.queryParams("reason"));
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
        if (reason_data == null) {
            return "-";
        }
        return reason_data.toString();
    }

    public String getTimeReported() {
        return timeReported;
    }

    @Override
    public String toString() {
        return "Id: " + alarm_id + " Time: " + getTimeReported();
    }
}
