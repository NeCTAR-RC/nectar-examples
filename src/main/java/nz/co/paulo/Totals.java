package nz.co.paulo;

import com.sun.management.OperatingSystemMXBean;

import spark.ModelAndView;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martinpaulo on 7/07/2014.
 */
public class Totals {

    private static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
            OperatingSystemMXBean.class);
    private static DecimalFormat format = new DecimalFormat("#.##");

    String cpuLoad;
    String systemCpuLoad;
    String averageLoad;

    public Totals() {
        averageLoad = format.format(osBean.getSystemLoadAverage());
        systemCpuLoad = format.format(osBean.getSystemCpuLoad());
        cpuLoad = format.format(osBean.getProcessCpuLoad());
    }

    public ModelAndView getTotals() {
        return new ModelAndView(this, "index.mustache");
    }

}
