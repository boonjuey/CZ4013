package model;
import java.net.InetAddress;
import static java.lang.Math.toIntExact;

public class Subscriber {
    private InetAddress address; 
    private int port; 
    private int duration;

    public Subscriber(InetAddress address, int port, int duration) {
        this.address = address; 
        this.port = port; 
        this.duration = duration; 
    }

    // public String getAddressAndPort(){
    //     String addressStr = address.getHostAddress();
    //     String portStr = Integer.toString(port);
    //     return addressStr + ":" + portStr;

    // }
    public InetAddress getAddress(){
        return address;
    }

    public int getPort(){
        return port;
    }

    public int getDuration(){
        return duration;
    }
}

