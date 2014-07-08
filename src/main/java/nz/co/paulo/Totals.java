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
    String disabled;

    public Totals(boolean isNoProcessRunning) {
        averageLoad = getFormatted(osBean.getSystemLoadAverage());
        systemCpuLoad = getFormatted(osBean.getSystemCpuLoad());
        cpuLoad = getFormatted(osBean.getProcessCpuLoad());
        this.disabled = isNoProcessRunning ? "" : "disabled";
    }

    private String getFormatted(double sample) {
        return format.format(sample);
    }

    public ModelAndView getTotals() {
        return new ModelAndView(this, "index.mustache");
    }

}
