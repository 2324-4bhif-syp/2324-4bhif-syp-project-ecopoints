package at.ecopoints.repository;

import at.ecopoints.entity.Trip;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TripRepository {
    @Inject
    EntityManager em;

    @Transactional
    public void save(Trip trip){
        em.persist(trip);
    }
    public Trip findById(Long id){
        return em.find(Trip.class, id);
    }
    @Transactional
    public void delete(Long id){
        em.remove(findById(id));
    }
    public List<Trip> getAll(){
        return em.createQuery("select t from Trip t", Trip.class).getResultList();
    }
    @Transactional
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
