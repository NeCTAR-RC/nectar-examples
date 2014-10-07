package nz.co.paulo;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/*
 * A basic demonstrator of a Trove instance being populated by Liquibase with a very basic
 * web application serving out the created content.
 * Created by Martin Paulo on 7/07/2014.
 */
public class Mainstay {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Mainstay.class);

    public static void main(String[] args) {
        setPort(8080);
        staticFileLocation("/public");
        loadMysqlDriver();
        get("/", (rq, rs) -> PM.getProducts(), new MustacheTemplateEngine());
        LOG.info("Spark fun fact: Main exits before a single page is served...");
    }

    private static void loadMysqlDriver() {
        try {
            LOG.info("Loading driver...");
            Class.forName("com.mysql.jdbc.Driver");
            LOG.info("Driver loaded!");
        } catch (Exception ex) {
            LOG.error("Couldn't load driver!", ex);
        }
    }

}
