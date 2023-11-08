package at.ecopoints.repository;

import at.ecopoints.entity.CarData;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CarDataRepositoryTest {
    @Inject
    CarDataRepository carDataRepository;

    @Test
    @Transactional
    void saveCarData() {
    }

    @Test
    void findCarDataById() {
    }

    @Test
    void deleteCarData() {
    }

    @Test
    void getAllCarDatas() {
    }

    @Test
    void updateCarData() {
    }
}