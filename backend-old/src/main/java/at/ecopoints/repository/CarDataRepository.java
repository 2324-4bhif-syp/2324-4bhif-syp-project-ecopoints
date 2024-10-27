package at.ecopoints.repository;

import at.ecopoints.entity.CarData;
import at.ecopoints.entity.DTO.CarDataEntry;
import at.ecopoints.entity.Trip;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CarDataRepository {
    @Inject
    EntityManager em;

    @Inject
    TripRepository tripRepository;

    public void save(CarDataEntry carDataEntry) {

        CarData carData = createCarData(carDataEntry);

        em.persist(carData);
    }

    private CarData createCarData(CarDataEntry carDataEntry) {
        Trip trip = tripRepository
                .findById(carDataEntry.tripId());

        return new CarData(
                carDataEntry.longitude(),
                carDataEntry.latitude(),
                carDataEntry.currentEngineRPM(),
                carDataEntry.currentVelocity(),
                carDataEntry.throttlePosition(),
                carDataEntry.engineRunTime(),
                carDataEntry.timeStamp(),
                trip
        );
    }

    public CarData findById(Long id) {
        return em.find(CarData.class, id);
    }

    public void delete(Long id) {
        em.remove(findById(id));
    }

    public List<CarData> getAll(){
        return em.createQuery("select c from CarData c", CarData.class).getResultList();
    }

    public void update(CarDataEntry carDataEntry, Long id){
        CarData carData = createCarData(carDataEntry);
        carData.setId(id);

        em.merge(carData);
    }
}
