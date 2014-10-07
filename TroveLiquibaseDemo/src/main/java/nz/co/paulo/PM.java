package nz.co.paulo;

import spark.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * An implementation of the Presentation Model pattern (http://martinfowler.com/eaaDev/PresentationModel.html).
 * Created by martin paulo on 3/10/2014.
 */
public class PM {

    private static final String SQL_PRODUCTS = "SELECT CODE, DESCRIPTION, VALUE FROM PRODUCTS";
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Mainstay.class);
    public static final String LIQUIBASE_PROPERTIES = "target/classes/connections/liquibase.properties";

    private static PM instance = null;

    final List<Row> rows = new ArrayList<>();

    private synchronized static PM getInstance() {
        if (instance == null) {
            instance = new PM();
        }
        return instance;
    }

    static class Row {

        static int rowCount = 0;

        /* hokey way of getting stripes on table */
        final String cssClass = getCssClass();

        final String code;
        final String value;
        final String description;

        private static String getCssClass() {
            rowCount = rowCount + 1;
            return ((rowCount % 2 != 0) ? "even" : "odd");
        }

        Row(String code, String value, String description) {
            this.code = code;
            this.value = value;
            this.description = description;
        }
    }

    /**
     * Simply connects to the database, and copies the data from the products table into the rows
     */
    private PM() {
        LOG.info("Building presentation model");
        try (Connection conn = DriverManager.getConnection(getDbUrl())) {
            try (Statement statement = conn.createStatement()) {
                try (ResultSet rs = statement.executeQuery(SQL_PRODUCTS)) {
                    while (rs.next()) {
                        String code = rs.getString("CODE");
                        String description = rs.getString("DESCRIPTION");
                        String value = rs.getString("VALUE");
                        rows.add(new Row(code, value, description));
                    }
                }
            }
        } catch (SQLException ex) {
            LOG.error("SQLException: " + ex.getMessage() +
                    " State: " + ex.getSQLState() +
                    " Vendor: " + ex.getErrorCode(), ex);
        }
    }

    private String getDbUrl() {
        Properties properties = new Properties();
        String result = "";
        try (InputStream is = new FileInputStream(new File(LIQUIBASE_PROPERTIES))) {
            properties.load(is);
            result = properties.getProperty("url") +
                    "?user=" + properties.getProperty("username") +
                    "&password=" + properties.getProperty("password");
        } catch (IOException ex) {
            LOG.error("Could not read properties...", ex);
        }
        return result;
    }

    public static ModelAndView getProducts() {
        return new ModelAndView(getInstance(), "index.mustache");
    }
}
