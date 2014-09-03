package nz.co.paulo;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.template.mustache.MustacheTemplateEngine;
import java.io.IOException;
import java.util.Arrays;

import static spark.Spark.*;

class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) throws Exception {
        return gson.toJson(model);
    }
}

/**
 * Created by Martin Paulo on 7/07/2014.
 */
public class Mainstay {

    private static int VALUE_CPU = 2;
    private static int VALUE_TIMEOUT = 4;

    private static ProcessBuilder pb;
    private static Process process;
    private static Object lock = new Object();

    // http://linux.die.net/man/1/stress
    private static String[] command = {"stress", "--cpu", "1", "--timeout", "600"};

    public static void main(String[] args) {
        pb = new ProcessBuilder();
        pb.redirectErrorStream(true);

        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as some javascript
        staticFileLocation("/public");
        get("/json", "application/json", (rq, rs) -> getPresentationModel(), new JsonTransformer());
        get("/", (rq, rs) -> getPresentationModel().getTotals(), new MustacheTemplateEngine());
        post("/stress", Mainstay::startStress);
    }

    private static PresentationModel getPresentationModel() {
        PresentationModel pm = new PresentationModel(isNoProcess());
        pm.setTimeout(command[VALUE_TIMEOUT]);
        pm.setIsDefault(command[VALUE_CPU]);
        return pm;
    }

    private static Response startStress(Request request, Response response) {
        System.out.printf("Starting  %s is:%n", Arrays.toString(command));
        synchronized (lock) {
            if (isNoProcess()) {
                command[VALUE_CPU] = request.queryParams("cpu_count");
                command[VALUE_TIMEOUT] = request.queryParams("timeout");
                pb.command(command);
                try {
                    process = pb.start();
                } catch (IOException e) {
                    process = null;
                    // we don't particularly care with this application.
                }
            }
        }
        response.redirect("/");
        return response;
    }

    private static boolean isNoProcess() {
        return process == null || !process.isAlive();
    }

// some of the things we can do with the process at a later date...
//           InputStream is = process.getInputStream();
//           InputStreamReader isr = new InputStreamReader(is);
//           BufferedReader br = new BufferedReader(isr);
//           String line;
//           System.out.printf("Output of running %s is:\n", Arrays.toString(command));
//           while ((line = br.readLine()) != null) {
//                System.out.println(line);
//           }
//
//           //Wait to get exit value
//          int exitValue = process.waitFor();
//          System.out.println("\n\nExit Value is " + exitValue);
//          process.waitFor(1, TimeUnit.SECONDS);
//          if (process.isAlive()) {
//                process.destroyForcibly();
//           }


}
