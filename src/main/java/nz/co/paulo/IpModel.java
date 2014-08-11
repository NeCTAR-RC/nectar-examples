package nz.co.paulo;

import spark.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martin paulo on 8/08/2014.
 */
public class IpModel {

    String ipAddress = getHostAddress();

    private String getHostAddress(){
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
}
