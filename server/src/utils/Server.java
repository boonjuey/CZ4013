package utils;

import java.net.DatagramSocket;
import java.util.Properties;

import controller.RequestController;
import controller.ResponseController;
import dao.FlightDao;
import invocationSemantics.AtLeastOnce;
import invocationSemantics.AtMostOnce;
import invocationSemantics.InvocationSemantics;
import model.Request;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public class Server extends Thread {
    private String server_host;
    private int server_port;
    private int timeout;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024];
    private InvocationSemantics invocationSemantics;

    public Server(String[] args) {
        FlightDao flightDao = new FlightDao();
        flightDao.readFile();
        RequestController requestController = new RequestController(flightDao);
        ResponseController responseController = new ResponseController();

        if (args.length > 0 && args[0].equals("atmostonce")) {
            invocationSemantics = new AtMostOnce(requestController, responseController);
        } else {
            invocationSemantics = new AtLeastOnce(requestController, responseController);
        }

        try {
            String configFilePath = System.getProperty("user.dir") + "\\server\\src\\utils\\config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);

            server_host = prop.getProperty("SERVER_HOST");
            server_port = Integer.parseInt(prop.getProperty("SERVER_PORT"));
            timeout = Integer.parseInt(prop.getProperty("TIMEOUT"));

            socket = new DatagramSocket(server_port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Running server...");
        running = true;

        try {
            while (running) {
                buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                long endTime = System.currentTimeMillis() + timeout * 1000;
                Request request = invocationSemantics.getRequest(packet);
                DatagramPacket response = null;

                try {
                    Object result = invocationSemantics.processRequest(request);
                    response = invocationSemantics.prepareResponse(request, result, null);
                } catch (Exception e) {
                    response = invocationSemantics.prepareResponse(request, null, e);
                }
                System.out.println("Response: " + new String(response.getData()));

                if (System.currentTimeMillis() <= endTime) {
                    socket.send(response);
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}