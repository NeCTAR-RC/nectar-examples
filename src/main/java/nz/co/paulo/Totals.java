package nz.co.paulo;

import com.sun.management.OperatingSystemMXBean;

import spark.ModelAndView;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martinpaulo on 7/07/2014.
 */
public class Totals {
    private static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
            OperatingSystemMXBean.class);

    public ModelAndView getTotals() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("cpuLoad", getCpuLoad());
        map.put("systemCpuLoad", getSystemCpuLoad());
        map.put("averageLoad", getAverageLoad());
        return new ModelAndView(map, "index.mustache");
    }

    private String getAverageLoad() {
        return String.valueOf(osBean.getSystemLoadAverage());
    }

    private String getSystemCpuLoad() {
        // What % load the overall system is at, from 0.0-1.0
        return String.valueOf(osBean.getSystemCpuLoad());
    }

    private String getCpuLoad() {
        // What % CPU load this current JVM is taking, from 0.0-1.0
        return String.valueOf(osBean.getProcessCpuLoad());
    }
}
