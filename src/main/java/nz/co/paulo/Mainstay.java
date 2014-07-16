package nz.co.paulo;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.IOException;
import java.util.ArrayList;

import static spark.Spark.*;
import static spark.Spark.get;

/**
 * Created by martinpaulo on 15/07/2014.
 */
public class Mainstay {

    public static void main(String[] args) {
        Context c = new Context();
        try {
            c.results.addAll(DiskTester.testStorage());
        } catch (IOException e) {
            c.results.add(new DiskTester.Row("Exception: " + e.getMessage()));
            for (StackTraceElement element: e.getStackTrace()) {
                c.results.add(new DiskTester.Row(element.toString()));
            }
        }
        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as some javascript
        staticFileLocation("/public");
        get("/", (rq, rs) -> new ModelAndView(c, "index.mustache"), new MustacheTemplateEngine());
    }

}

class Context {

    ArrayList<DiskTester.Row> results = new ArrayList<>();

}
