package at.ecopoints.repository;

import at.ecopoints.entity.Trip;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TripRepository {
    @Inject
    EntityManager em;

    public void save(Trip trip){
        em.persist(trip);
    }
    public Trip findById(UUID id){
        return em.find(Trip.class, id);
    }

    public void delete(UUID id){
        em.remove(findById(id));
    }
    public List<Trip> getAll(){
        return em.createQuery("select t from Trip t", Trip.class).getResultList();
    }

    public void update(Trip trip){
        Trip tr = findById(trip.getId());

        tr.setId(trip.getId());
        tr.setDate(trip.getDate());
        tr.setDistance(trip.getDistance());
        tr.setAvgSpeed(trip.getAvgSpeed());
        tr.setAvgEngineRotation(trip.getAvgEngineRotation());
        tr.setRewardedEcoPoints(trip.getRewardedEcoPoints());

        em.merge(tr);
    }
}
