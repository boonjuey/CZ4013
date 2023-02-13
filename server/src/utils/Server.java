package utils;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public class Server extends Thread {

    private String server_host;
    private int server_port;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public Server() {
        try {
            String configFilePath = System.getProperty("user.dir") + "\\server\\src\\utils\\config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);

            server_host = prop.getProperty("SERVER_HOST");
            server_port = Integer.parseInt(prop.getProperty("SERVER_PORT"));
            
            socket = new DatagramSocket(server_port);
        }catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public void run() {
        try {
            System.out.println("Running server...");
            running = true;
        
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                if (received.equals("end")) {
                    running = false;
                    continue;
                }


                String replyString = "Messaged received by Java server";
                buf = replyString.getBytes();
                DatagramPacket reply = new DatagramPacket(buf, buf.length, address, port);
                System.out.println("Received from client:" + received + " at port: " + port);
                socket.send(reply);
            }
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
}