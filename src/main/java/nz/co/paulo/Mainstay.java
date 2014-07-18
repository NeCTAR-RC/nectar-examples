package nz.co.paulo;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.IOException;
import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by martinpaulo on 15/07/2014.
 */
public class Mainstay {

    public static void main(String[] args) {
        Context c = new Context();

        new Thread(() -> {
            try {
                DiskTester.testStorage(c.results);
            } catch (IOException e) {
                c.results.add(new DiskTester.Row("Exception: " + e.getMessage()));
                for (StackTraceElement element : e.getStackTrace()) {
                    c.results.add(new DiskTester.Row(element.toString()));
                }
            }
        }).run();
        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as some javascript
        staticFileLocation("/public");
        get("/", (rq, rs) -> new ModelAndView(c.clone(), "index.mustache"), new MustacheTemplateEngine());
    }

}

class Context {

    ArrayList<DiskTester.Row> results = new ArrayList<>();

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public synchronized Context clone() throws CloneNotSupportedException {
        Context copy = new Context();
        copy.results.addAll(results);
        return copy;
    }

}
