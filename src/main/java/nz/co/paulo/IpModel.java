package nz.co.paulo;

import spark.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martin paulo on 8/08/2014.
 */
public class IpModel {

    String ipAddress = getHostAddress();

    // Martin: is this is a bad idea? As this is this machines IP address: Not the IP address of the server being
    // monitored by ceilometer, hopefully.
    private String getHostAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // we'll just go with the default IP of localhost...
        }
        return "127.0.0.1";
    }

    public ModelAndView getTotals() {
            return new ModelAndView(this, "index.mustache");
    }
}
