package model;
import java.net.InetAddress;
import static java.lang.Math.toIntExact;

public class Subscription {
    private int duration;
    private int flightId;

    public Subscription(int flightId, int duration) {
        this.flightId = flightId;
        this.duration = duration; 
    }

    // public String getAddressAndPort(){
    //     String addressStr = address.getHostAddress();
    //     String portStr = Integer.toString(port);
    //     return addressStr + ":" + portStr;

    // }
   
    public int getFlightId(){
        return flightId;
    }
    public int getDuration(){
        return duration;
    }
}

