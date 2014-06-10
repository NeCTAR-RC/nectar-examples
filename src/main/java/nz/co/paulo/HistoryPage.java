package nz.co.paulo;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Martin Paulo on 10/06/2014.
 */
public class HistoryPage {
    public static final String PAGE_BEFORE_TABLE = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<title>Alarms Recorded</title>" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"mainstay.css\">" +
            "</head>" +
            "<h2>Recorded so far</H2>" +
            "<table>" +
            "<tr><th>Alarm</th><th>Times Called</th></tr>";

    private Map<String, Integer> alarmTotals = new TreeMap<>();
    private String result;
    private int rowCount;

    public HistoryPage(Map<String, Integer> alarmTotals) {
        alarmTotals.forEach((source, value) -> {
            this.alarmTotals.put(source, value);
        });
    }

    @Override
    public String toString() {
        result = PAGE_BEFORE_TABLE;
        rowCount = 0;
        alarmTotals.forEach((source, value) -> {
            rowCount++;
            result += "<tr" + getCssClass() + "><td>" + source + "</td><td>" + value + "</td></tr>";
        });
        result += "</table>";
        return result;
    }

    private String getCssClass() {
        return ((rowCount % 2 != 0) ? "" : " class=\"alt\"");
    }

}
