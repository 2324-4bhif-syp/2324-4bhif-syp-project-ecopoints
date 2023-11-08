package at.ecopoints.boundary;

import at.ecopoints.entity.CarData;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;


import static io.restassured.RestAssured.given;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.equalTo;


@QuarkusTest
class CarDataResourceTest {
    @AfterEach
    void cleanup() {
        Response response = given().when().get("/carData/1");

        if (response.getStatusCode() == 200) {
            given().when().delete("/carData/{id}", "1");
        }
    }

    @Test
    void addAndGetCarDataById() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "trip_id": "5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f",
                      "longitude": 45.123456,
                      "latitude": 34.567890,
                      "current_engine_rpm": 3000.0,
                      "current_velocity": 60.0,
                      "throttle_position": 75.0,
                      "engine_run_time": "14:30:15",
                      "time_stamp": "2023-11-08T13:45:00.000Z"
                    }
                    """
                ).when()
                .post("/carData")
                .then()
                .statusCode(201);

        Response response = given()
                .when()
                .get("/carData/1");

        response.then()
                .statusCode(200);

        response.then()
                .body("trip_id", equalTo("5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f"))
                .body("longitude", equalTo(45.123456f))
                .body("latitude", equalTo(34.567890f))
                .body("current_engine_rpm", equalTo(3000.0f))
                .body("current_velocity", equalTo(60.0f))
                .body("throttle_position", equalTo(75.0f))
                .body("engine_run_time", equalTo("14:30:15"))
                .body("time_stamp", equalTo("2023-11-08T13:45:00.000+00:00"));
    }

    @Test
    void getAllCarData() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "trip_id": "5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f",
                      "longitude": 45.123456,
                      "latitude": 34.567890,
                      "current_engine_rpm": 3000.0,
                      "current_velocity": 60.0,
                      "throttle_position": 75.0,
                      "engine_run_time": "14:30:15",
                      "time_stamp": "2023-11-08T13:45:00.000Z"
                    }
                    """
                ).when()
                .post("/carData")
                .then()
                .statusCode(201);

        Response response = given()
                .when()
                .get("/carData");

        assertThat(response.then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", CarData.class)).hasSize(1);

        List<CarData> carDataList = response.then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", CarData.class);


        assertThat(carDataList.get(0).getTripId().toString()).isEqualTo("5d59e169-94a4-4bf7-8a8e-6a52a1d85f0f");
        assertThat(carDataList.get(0).getLongitude()).isEqualTo(45.123455);
        assertThat(carDataList.get(0).getLatitude()).isEqualTo(34.567890);
        assertThat(carDataList.get(0).getCurrentEngineRPM()).isEqualTo(3000.0);
        assertThat(carDataList.get(0).getCurrentVelocity()).isEqualTo(60.0);
        assertThat(carDataList.get(0).getThrottlePosition()).isEqualTo(75.0);
        assertThat(carDataList.get(0).getEngineRunTime()).isEqualTo("14:30:15");
        assertThat(carDataList.get(0).getTimeStamp()).isEqualTo(Timestamp.valueOf("2023-11-08 14:45:00.0000"));

    }

    @Test
    void saveCarData() {
    }

    @Test
    void deleteCarData() {
    }

    @Test
    void updateCarData() {
    }
}