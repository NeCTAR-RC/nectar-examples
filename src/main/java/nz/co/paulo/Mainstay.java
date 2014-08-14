package nz.co.paulo;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import static spark.Spark.*;

/**
 * An application to learn a little about Spark, and a little bit about ceilometer alarms
 * Created by Martin Paulo on 19/05/2014.
 */
public class Mainstay {

    private static final Map<String, Integer> totals = new ConcurrentSkipListMap<>();
    private static final Map<String, History> history = new ConcurrentSkipListMap<>();
    public static final String URL_TOTALS = "/totals";
    public static final String URL_ALARM = "/alarm";
    private static final String URL_VIEW = "/view";

    public static void main(String[] args) {
        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here
        staticFileLocation("/public");
        // we will allow users to register an alarm via a form
        get(URL_ALARM, (rq, rs) -> new ModelAndView(new HashMap(), "form.mustache"), new MustacheTemplateEngine());
        // hence we want to handle the form post...
        post(URL_ALARM, Mainstay::alarmFromForm);
        // and the actual alarms posts from ceilometer...
        post(URL_ALARM + "/:name", Mainstay::alarmFromCeilometer);
        // which we can view
        get(URL_VIEW + "/:name", Mainstay::alarmView, new MustacheTemplateEngine());
        // and we can view the totals of the alarm call as well
        get(URL_TOTALS, (rq, rs) -> new Totals().getTotals(totals), new MustacheTemplateEngine());
        // of course we might want reset the totals and the history
        get("/clear", Mainstay::clearAll);
        // and remove the entire set of totals and history.
        get("/reset", Mainstay::resetTotals);
        get("/", (rq, rs) -> IpModel.getInstance().getTotals(), new MustacheTemplateEngine());
    }

    private static ModelAndView alarmView(Request request, Response response) {
        String alarmName = request.params(":name");
        System.out.println("Alarm: " + alarmName);
        return new ModelAndView(history.get(alarmName), "view.mustache");
    }

    private static Response clearAll(Request request, Response response) {
        totals.clear();
        history.clear();
        response.redirect(URL_TOTALS);
        return response;
    }

    private static Response resetTotals(Request request, Response response) {
        totals.keySet().forEach((key) -> totals.put(key, 0));
        history.values().forEach((entry) -> entry.getHistory().clear());
        response.redirect(URL_TOTALS);
        return response;
    }

    private static Response alarmFromCeilometer(Request request, Response response) {
        registerAlarm(request.params(":name"), request);
        response.status(HttpServletResponse.SC_OK);
        return response;
    }

    private static Response alarmFromForm(Request request, Response response) {
        registerAlarm(request.queryParams("alarm_name"), request);
        response.redirect(URL_TOTALS);
        return response;
    }

    private static void registerAlarm(String source, Request request) {
        if (source == null || source.length() <= 0) {
            source = "unknown alarm?";
        }
        totals.put(source, totals.getOrDefault(source, 0) + 1);
        AlarmDetails alarmDetails = new AlarmDetails(request);
        History priorAlarms = history.get(source);
        if (priorAlarms == null) {
            priorAlarms = new History(source);
            history.put(source, priorAlarms);
        }
        priorAlarms.getHistory().add(alarmDetails);
    }
}
