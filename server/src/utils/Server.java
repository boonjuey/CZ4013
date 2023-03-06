package utils;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.io.File;

import controller.CallbackController;
import controller.RequestController;
import controller.ResponseController;
import dao.FlightDao;
import errors.FlightNotFoundException;
import errors.NoSubscriptionFoundException;
import invocationSemantics.AtLeastOnce;
import invocationSemantics.AtMostOnce;
import invocationSemantics.InvocationSemantics;
import model.Flight;
import model.Request;
import model.Subscriber;
import model.Response;
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
    private CallbackController callbackController;
    private FlightDao flightDao;

    public Server(String[] args) {
        this.flightDao = new FlightDao();
        flightDao.readFile();
        this.callbackController = new CallbackController();
        RequestController requestController = new RequestController(flightDao, callbackController);
        ResponseController responseController = new ResponseController();

        if (args.length > 0 && args[0].equals("atmostonce")) {
            invocationSemantics = new AtMostOnce(requestController, responseController);
        } else {
            invocationSemantics = new AtLeastOnce(requestController, responseController);
        }

        try {
            // String configFilePath = System.getProperty("user.dir") +
            // "\\server\\src\\utils\\config.properties";
            // String configFilePath = System.getProperty("user.home") +
            // "/Desktop/CZ4013/server/src/utils/config.properties";
            String configFilePath = System.getProperty("user.home")
                    + "/Desktop/CZ4013/server/src/utils/config.properties";

            // String configFilePath = System.getProperty("user.home") + File.separator +
            // "Desktop" + File.separator + "CZ4013" + File.separator + "server" +
            // File.separator + "src" + File.separator + "utils" + File.separator +
            // "config.properties";
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
                System.out.println("Request: " + request.getFlightId());
                DatagramPacket response = null;

                try {
                    Object result = invocationSemantics.processRequest(request);
                    System.out.println("Result: " + result);
                    response = invocationSemantics.prepareResponse(request, result, null);
                } catch (Exception e) {
                    System.out.println(e);
                    response = invocationSemantics.prepareResponse(request, null, e);
                }

                System.out.println("Response: " + new String(response.getData()));
                if (System.currentTimeMillis() <= endTime) {
                    socket.send(response);
                }
                System.out.println(request.getRequestType());
                if (request.getRequestType() == 2) {
                    int size = callbackController.getSubscriberSizeByFlightId(request.getFlightId());
                    ArrayList<Subscriber> subscribers2 = callbackController
                            .getSubscribersByFlightId(request.getFlightId());
                    if (size > 0) {
                        System.out.printf("Size: %d", size);
                        for (Subscriber subscriber : subscribers2) {
                           
                            int seats = flightDao.getFlightById(request.getFlightId()).getAvailableSeats();
                            Response subResponse = new Response(new HashMap<String, Object>() {
                                {
                                    put("result", Integer.toString(seats) + " left for" + " flight "
                                            + Integer.toString(request.getFlightId()));
                                }
                            });
                            byte[] marshalledResponse = new Marshaller().marshal(subResponse.getResponseBody());
                            DatagramPacket responsePacket = new DatagramPacket(marshalledResponse,
                                    marshalledResponse.length,
                                    subscriber.getAddress(), subscriber.getPort());

                            socket.send(responsePacket);
                        }
                        System.out.println(callbackController.getSubscriberSizeByFlightId(1));

                    }
                }
                System.out.println("Size after");
                System.out.println(callbackController.getSubscriberSizeByFlightId(1));
               
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FlightNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}