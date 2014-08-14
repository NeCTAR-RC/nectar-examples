package nz.co.paulo;

import spark.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Martin Paulo on 11/08/2014.
 * A holder for the details passed in a Ceilometer alarm call
 */
public class AlarmDetails {

    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    final String alarmId;
    final String previous;
    final String current;
    final String reason;
    final String reasonData;
    final String timeReported;

    public AlarmDetails(Request request) {
        Date today = Calendar.getInstance().getTime();
        timeReported = df.format(today);
        alarmId = getNonNullValue(request.queryParams("alarm_id"));
        previous = getNonNullValue(request.queryParams("previous"));
        current = getNonNullValue(request.queryParams("current"));
        reason = getNonNullValue(request.queryParams("reason"));
        reasonData = getNonNullValue(request.queryParams("reason_data"));
        Set<String> strings = request.queryParams();
        for (String param: strings) {
            System.out.println("GOT: " + param);
        }
        Map<String, String> params = request.params();
        for (String param: params.keySet()) {
            System.out.println("Param: " + param);
        }
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
