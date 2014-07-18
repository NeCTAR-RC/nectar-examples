package nz.co.paulo;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Created by martinpaulo on 15/07/2014.
 */
public class Mainstay {

    public static void main(String[] args) {
        DiskTester.testStorage();
        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as some javascript
        staticFileLocation("/public");
        get("/", (rq, rs) -> new ModelAndView(DiskTester.getResults(), "index.mustache"), new MustacheTemplateEngine());
    }

}


