package nz.co.paulo;

import spark.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martin paulo on 8/08/2014.
 * A pretty basic attempt to find the IP address of the machine we are running on.
 */
class IpModel {

    final String ipAddress = getHostAddress();

    private static IpModel instance;

    private IpModel() {
        // a singleton.
    }

    private String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // we'll just go with the default IP of localhost...
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    public ModelAndView getTotals() {
        return new ModelAndView(this, "index.mustache");
    }

    public synchronized static IpModel getInstance() {
        if (instance == null) {
            instance = new IpModel();
        }
        return instance;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
