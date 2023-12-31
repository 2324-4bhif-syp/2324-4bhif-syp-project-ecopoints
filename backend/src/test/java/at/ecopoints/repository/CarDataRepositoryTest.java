package at.ecopoints.repository;

import at.ecopoints.entity.CarData;
import at.ecopoints.entity.DTO.CarDataEntry;
import at.ecopoints.entity.Trip;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;


@QuarkusTest
class CarDataRepositoryTest {
    @Inject
    CarDataRepository carDataRepository;

    @AfterEach
    @Transactional
    void cleanup() {
        for(CarData cd : carDataRepository.getAll()){
            carDataRepository.delete(cd.getId());
        }
    }

    /*

    @Test
    @Transactional
    void saveFindByIdAndDeleteCarData() {
        CarData carData = new CarData();
        Trip trip = createSampleTrip();

        carData.setLongitude(45.123456);
        carData.setLatitude(34.567890);
        carData.setCurrentEngineRPM(3000.0);
        carData.setCurrentVelocity(60.0);
        carData.setThrottlePosition(75.0);
        carData.setEngineRunTime("14:30:15");
        carData.setTimeStamp(Timestamp.valueOf("2023-11-08 13:45:00"));
        carData.setTrip(trip);

        carDataRepository.save(carData);

        CarData cd = carDataRepository.findById(carData.getId());

        assertThat(cd.getTrip().getId().toString())
                .isEqualTo("5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f");
        assertThat(cd.getLongitude())
                .isEqualTo(45.123456);
        assertThat(cd.getLatitude())
                .isEqualTo(34.567890);
        assertThat(cd.getCurrentEngineRPM())
                .isEqualTo(3000.0);
        assertThat(cd.getCurrentVelocity())
                .isEqualTo(60.0);
        assertThat(cd.getThrottlePosition())
                .isEqualTo(75.0);
        assertThat(cd.getEngineRunTime())
                .isEqualTo("14:30:15");
        assertThat(cd.getTimeStamp())
                .isEqualTo(Timestamp.valueOf("2023-11-08 13:45:00"));

        carDataRepository.delete(carData.getId());

        assertThat(carDataRepository.findById(1L))
                .isNull();
    }


    @Test
    @Transactional
    void addTwoCarDatasAndGetAll() {

        CarData carData = new CarData();
        Trip trip = createSampleTrip();

        carData.setLongitude(45.123456);
        carData.setLatitude(34.567890);
        carData.setCurrentEngineRPM(3000.0);
        carData.setCurrentVelocity(60.0);
        carData.setThrottlePosition(75.0);
        carData.setEngineRunTime("14:30:15");
        carData.setTimeStamp(Timestamp.valueOf("2023-11-08 13:45:00"));
        carData.setTrip(trip);

        carDataRepository.save(carData);

        CarData carData2 = new CarData();

        carData2.setLongitude(39.123456);
        carData2.setLatitude(30.567890);
        carData2.setCurrentEngineRPM(2000.0);
        carData2.setCurrentVelocity(50.0);
        carData2.setThrottlePosition(25.0);
        carData2.setEngineRunTime("10:48:15");
        carData2.setTimeStamp(Timestamp.valueOf("2023-11-08 10:45:00"));
        carData2.setTrip(trip);

        carDataRepository.save(carData2);

        List<CarData> carDataList = carDataRepository.getAll();

        assertThat(carDataList.get(0).getTrip().getId().toString())
                .isEqualTo("5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f");
        assertThat(carDataList.get(0).getLongitude())
                .isEqualTo(45.123456);
        assertThat(carDataList.get(0).getLatitude())
                .isEqualTo(34.567890);
        assertThat(carDataList.get(0).getCurrentEngineRPM())
                .isEqualTo(3000.0);

        assertThat(carDataList.get(1).getTrip().getId().toString())
                .isEqualTo("2d46e165-94a4-4bf7-8a8e-6a52a1d85f0f");
        assertThat(carDataList.get(1).getLongitude())
                .isEqualTo(39.123456);
        assertThat(carDataList.get(1).getLatitude())
                .isEqualTo(30.567890);
        assertThat(carDataList.get(1).getCurrentEngineRPM())
                .isEqualTo(2000.0);

        carDataRepository.delete(carData.getId());
        carDataRepository.delete(carData2.getId());
    }

    @Test
    @Transactional
    void updateCarData() {
        CarDataEntry carDataEntry = new CarDataEntry(
              45.123455,
                34.567890,
                3000.0,
                60.0,
                75.0,
                "14:30:15",
                Timestamp.valueOf("2023-11-08 13:45:00"),
                UUID.fromString("5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f")
        );
        Trip trip = createSampleTrip();

        carDataRepository.save(carDataEntry);

        carDataEntry.

        carDataEntry.setLongitude(50.0);
        carDataEntry.setLatitude(40.0);
        carDataEntry.setCurrentEngineRPM(4000.0);

        carDataRepository.update(carDataEntry);

        CarData updatedCarData = carDataRepository.findById(carDataEntry.getId());

        assertThat(updatedCarData.getTrip().getId().toString())
                .isEqualTo("5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f");
        assertThat(updatedCarData.getLongitude())
                .isEqualTo(50.0);
        assertThat(updatedCarData.getLatitude())
                .isEqualTo(40.0);
        assertThat(updatedCarData.getCurrentEngineRPM())
                .isEqualTo(4000.0);
    }

    private Trip createSampleTrip(){
        return new Trip(
                UUID.fromString("5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f"),
                15.0,
                23.4,
                2200.0,
                new Date(),
                100.0
        );
    }

    */

}