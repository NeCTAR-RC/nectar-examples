package nz.co.paulo;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static spark.Spark.*;

/**
 * An application to learn a little about Spark, and a little bit about ceilometer alarms
 * Created by Martin Paulo on 19/05/2014.
 */
public class Mainstay {

    private static final Map<String, Integer> totals = new ConcurrentSkipListMap();
    private static final Map<String, ArrayList<AlarmDetails>> history = new ConcurrentSkipListMap<>();
    public static final String URL_TOTALS = "/totals";
    public static final String URL_ALARM = "/alarm";

    public static void main(String[] args) {
        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as the home page
        staticFileLocation("/public");
        // we will allow users to register an alarm via a form
        get(URL_ALARM, (rq, rs) -> new ModelAndView(new HashMap(), "form.mustache"), new MustacheTemplateEngine());
        // hence we want to handle the form post...
        post(URL_ALARM, Mainstay::alarmFromForm);
        // and the actual alarms themselves...
        post(URL_ALARM + "/:name", Mainstay::alarmRegistered);
        // and we can view the history of our alarms
        get(URL_TOTALS, (rq, rs) -> new Totals().getTotals(totals), new MustacheTemplateEngine());
        get("/clear", (rq, rs) -> {
            totals.clear();
            rs.redirect(URL_TOTALS);
            return rs;
        });
        get("/reset", Mainstay::resetTotals);
        get("/", (rq, rs) -> IpModel.getInstance().getTotals(), new MustacheTemplateEngine());
    }

    private static Response resetTotals(Request request, Response response) {
        totals.keySet().forEach((key) -> totals.put(key, 0));
        response.redirect(URL_TOTALS);
        return response;
    }

    private static Response alarmRegistered(Request request, Response response) {
        countAlarm(request.params(":name"), request);
        response.status(HttpServletResponse.SC_OK);
        return response;
    }

    private static Response alarmFromForm(Request request, Response response) {
        countAlarm(request.queryParams("alarm_name"), request);
        response.redirect(URL_TOTALS);
        return response;
    }

    private static void countAlarm(String source, Request request) {
        if (source == null || source.length() <= 0) {
            source = "unknown alarm?";
        }
        totals.put(source, totals.getOrDefault(source, 0) + 1);
        AlarmDetails alarmDetails = new AlarmDetails(request);
        ArrayList<AlarmDetails> priorAlarms = history.get(source);
        if (priorAlarms == null) {
            priorAlarms = new ArrayList<>();
            history.put(source, priorAlarms);
        }
        priorAlarms.add(alarmDetails);
    }
}
