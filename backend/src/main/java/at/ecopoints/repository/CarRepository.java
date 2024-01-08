package at.ecopoints.repository;

import at.ecopoints.entity.Car;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class CarRepository {

    @Inject
    EntityManager em;

    public void save(Car car) {
        em.persist(car);
    }

    public Car findById(Long id) {
        return em.find(Car.class, id);
    }

    public void delete(Long id) {
        em.remove(findById(id));
    }

    public void update(Car car){
        em.merge(car);
    }

    public List<Car> getAll(){
        return em.createNamedQuery("Car.findAll", Car.class).getResultList();
    }
}
