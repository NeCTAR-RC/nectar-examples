package nz.co.paulo;

import com.sun.management.OperatingSystemMXBean;
import spark.ModelAndView;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

/**
 * Created by martinpaulo on 7/07/2014.
 */
public class PresentationModel {

    private static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
            OperatingSystemMXBean.class);
    private static DecimalFormat format = new DecimalFormat("#.##");

    String cpuLoad;
    /**
     * The "recent cpu usage" for the whole system.
     * This value is a double in the [0.0,1.0] interval.
     * A value of 0.0 means that all CPUs were idle during the recent period of time observed,
     * while a value of 1.0 means that all CPUs were actively running 100% of the time during the
     * recent period being observed.
     * All values between 0.0 and 1.0 are possible depending of the activities going on in the system.
     * If the system recent cpu usage is not available, the method returns a negative value.
     */
    String systemCpuLoad;
    /**
     * the system load average for the last minute. The system load average is the sum of the number
     * of runnable entities queued to the available processors and the number of runnable entities
     * running on the available processors averaged over a period of time.
     * The way in which the load average is calculated is operating system specific but is
     * typically a damped time-dependent average.
     */
    String averageLoad;
    String disabled;
    String timeout;
    boolean oneIsDefault;
    boolean twoIsDefault;
    boolean fourIsDefault;
    boolean eightIsDefault;


    public PresentationModel(boolean isNoProcessRunning) {
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

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public void setIsDefault(String isDefault) {
        oneIsDefault = false;
        twoIsDefault = false;
        fourIsDefault = false;
        eightIsDefault = false;
        switch (isDefault) {
            case "1":
                oneIsDefault = true;
                break;
            case "2":
                twoIsDefault = true;
                break;
            case "4":
                fourIsDefault = true;
                break;
            case "8":
                eightIsDefault = true;
                break;
        }
    }
}
