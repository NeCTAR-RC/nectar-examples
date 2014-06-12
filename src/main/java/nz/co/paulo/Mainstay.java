package nz.co.paulo;

import spark.Request;
import spark.Route;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

/**
 * An application for me to learn a little about Spark, and a little bit about ceilometer alarms
 * Created by Martin Paulo on 19/05/2014.
 */
public class Mainstay {

    private static final Map<String, Integer> totals = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here
        staticFileLocation("/public");
        // alarms will be of the form:
        // http://www.somesite.com/alarm?source=some_value
        // where source and some_value can vary
        get("/alarm", addToAlarmTotals());
        // and we can view the history of our alarms at http://www.somesite.com/history
        get("/history", (request, response) -> new HistoryPage(totals).toString());
        // and we now want to check what happens on a post...
        post("/alarm", newAlarmSounded());
    }

    private static Route addToAlarmTotals() {
        return (request, response) -> {
            String source = recordAlarm(request);
            // just how do we respond to a ceilometer alarm?
            return "Hello World! This is lambda responding to your alarm: " + source + "...";
        };
    }

    private static String recordAlarm(Request request) {
        String source = request.queryParams("source");
        // we got a request that doesn't match our simple scheme...
        if (source == null) {
            source = "unknown?";
        }
        totals.put(source, totals.getOrDefault(source, 0) + 1);
        return source;
    }

    private static Route newAlarmSounded() {
        return (request, response) -> {
            recordAlarm(request);
            response.status(200);
            return response;
        };
    }

}
