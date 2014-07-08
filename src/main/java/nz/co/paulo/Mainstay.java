package nz.co.paulo;


import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

/**
 * For more complex info consider: https://support.hyperic.com/display/SIGAR/Home
 * Created by Martin Paulo on 7/07/2014.
 */
public class Mainstay {

    private static ProcessBuilder pb;
    private static Process process;
    private static String[] command = {"stress", "--cpu 1", "-timeout 60"};

    public static void main(String[] args) {
        pb = new ProcessBuilder();
        pb.redirectErrorStream(true);
        // http://linux.die.net/man/1/stress
        pb.command(command);

        // we'll run on port 8080
        setPort(8080);
        // we'll serve a css file from here, as well as the home page
        staticFileLocation("/public");
        get("/", "*", (rq, rs) -> new Totals().getTotals(), new MustacheTemplateEngine());
        post("/stress", Mainstay::startStress);
    }


    // http://www.xyzws.com/Javafaq/how-to-run-external-programs-by-using-java-processbuilder-class/189
    private static Response startStress(Request request, Response response) {
        if (process == null || !process.isAlive()) {
            try {
                System.out.println("Starting...");
                process = pb.start();
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                System.out.printf("Output of running %s is:\n",
                        Arrays.toString(command));
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }

                //Wait to get exit value

                    int exitValue = process.waitFor();
                    System.out.println("\n\nExit Value is " + exitValue);
            } catch (IOException e) {
                process = null;
                e.printStackTrace();
            } catch (InterruptedException e) {
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
}
