package at.ecopoints.repository;

import at.ecopoints.entity.CarData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class CarDataRepository {
    @Inject
    EntityManager em;

    public void save(CarData carData) {
        em.persist(carData);
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

    public void update(CarData carData){
        CarData cd = findById(carData.getId());

        cd.setTripId(carData.getTripId());
        cd.setLatitude(carData.getLatitude());
        cd.setLongitude(carData.getLongitude());
        cd.setCurrentVelocity(carData.getCurrentVelocity());
        cd.setCurrentEngineRPM(carData.getCurrentEngineRPM());
        cd.setEngineRunTime(carData.getEngineRunTime());
        cd.setTimeStamp(carData.getTimeStamp());
        cd.setThrottlePosition(carData.getThrottlePosition());

        em.merge(cd);
    }
}
