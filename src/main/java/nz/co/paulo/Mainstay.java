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

    private static final Map<String, Integer> totals = new ConcurrentSkipListMap();

    public static void main(String[] args) {
        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as the home page
        staticFileLocation("/public");
        // we will allow users to register an alarm via a form
        get("/alarm", (rq, rs) -> new ModelAndView(new HashMap(), "form.mustache"), new MustacheTemplateEngine());
        // hence we want to handle the form post...
        post("/alarm", Mainstay::alarmFromForm);
        // and the actual alarms themselves...
        post("alarm/:name", Mainstay::alarmRegistered);
        // and we can view the history of our alarms
        get("/history", (rq, rs) -> new History().getHistory(totals), new MustacheTemplateEngine());
        get("/clear", (rq, rs) -> {totals.clear(); rs.redirect("/history"); return rs;});
        get("/reset", Mainstay::resetTotals);
    }

    private static Response resetTotals(Request request, Response response) {
        totals.keySet().forEach((key)->totals.put(key, 0));
        response.redirect("/history");
        return response;
    }

    private static Response alarmRegistered(Request request, Response response) {
        countAlarm(request.params(":name"));
        response.status(HttpServletResponse.SC_OK);
        return response;
    }

    private static Response alarmFromForm(Request request, Response response) {
        countAlarm(request.queryParams("alarm_name"));
        response.redirect("/history");
        return response;
    }

    private static void countAlarm(String source) {
        if (source == null || source.length() <= 0) {
            source = "unknown alarm?";
        }
        totals.put(source, totals.getOrDefault(source, 0) + 1);
    }

}
