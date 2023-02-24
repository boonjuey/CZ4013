package dao;

import java.util.List;
import java.util.ArrayList;
import model.Flight;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FlightDao {
    private List<Flight> flights = new ArrayList<>();
    private File file = new File(System.getProperty("user.dir") + "\\server\\src\\data\\flights.txt");

    public void readFile() {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] flightData = data.split(",");
                flights.add(new Flight(
                        Integer.parseInt(flightData[0]),
                        flightData[1],
                        flightData[2],
                        Integer.parseInt(flightData[3]),
                        Float.parseFloat(flightData[4]),
                        Integer.parseInt(flightData[5])));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }
}
