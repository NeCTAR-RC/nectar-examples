package nz.co.paulo;


import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    // http://linux.die.net/man/1/stress
    private static String[] command = {"stress", "--cpu", "1", "--timeout", "10"};

    public static void main(String[] args) {
        pb = new ProcessBuilder();
        pb.redirectErrorStream(true);

        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as the home page
        staticFileLocation("/public");
        get("/json", "application/json", (rq, rs) -> new Totals(isNoProcess()), new JsonTransformer());
        get("/", (rq, rs) -> new Totals(isNoProcess()).getTotals(), new MustacheTemplateEngine());
        post("/stress", Mainstay::startStress);
    }

    private static Response startStress(Request request, Response response) {
        if (isNoProcess()) {
            try {
                command[VALUE_CPU] = request.queryParams("cpu_count");
                command[VALUE_TIMEOUT] = request.queryParams("timeout");
                pb.command(command);
//                System.out.printf("Starting  %s is:\n", Arrays.toString(command));
                process = pb.start();
//                InputStream is = process.getInputStream();
//                InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader br = new BufferedReader(isr);
//                String line;
//                System.out.printf("Output of running %s is:\n", Arrays.toString(command));
//                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
//                }
//
//                //Wait to get exit value
//                int exitValue = process.waitFor();
//                System.out.println("\n\nExit Value is " + exitValue);
            } catch (IOException e) {
                process = null;
                e.printStackTrace();
            }
//            process.waitFor(1, TimeUnit.SECONDS);
//            if (process.isAlive()) {
//                process.destroyForcibly();
//            }

        }
        response.redirect("/");
        return response;
    }

    private static boolean isNoProcess() {
        return process == null || !process.isAlive();
    }
}
