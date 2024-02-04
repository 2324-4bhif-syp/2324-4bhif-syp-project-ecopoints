package at.htl.ecopoints.csvData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import at.htl.ecopoints.db.DBHelper;
import at.htl.ecopoints.model.Trip;

public class ReadCsv {

    public static List<Trip> readTripCsv() {

        List<Trip> trips = new ArrayList<Trip>();

        try {
            List<String> lines = Files.readAllLines(Paths.get("./src/main/csvData/trip.csv"));

            for (String line : lines) {
                String[] parts = line.split(";");

                UUID id = UUID.fromString(parts[0]);
                double distance = Double.parseDouble(parts[1]);
                double avgSpeed = Double.parseDouble(parts[2]);
                double avgEngineRotation = Double.parseDouble(parts[3]);
                Date date = new Date(parts[4]);
                double rewardedEcoPoints = Double.parseDouble(parts[5]);

                Trip trip = new Trip(id, distance, avgSpeed, avgEngineRotation, date, rewardedEcoPoints);
                trips.add(trip);
            }

        }catch (IOException e) {
            throw new RuntimeException("Error reading trip.csv", e);
        }
        return trips;
    }
}
