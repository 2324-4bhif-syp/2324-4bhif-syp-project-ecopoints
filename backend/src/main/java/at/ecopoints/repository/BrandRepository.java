package at.ecopoints.repository;

import at.ecopoints.entity.Brand;
import at.ecopoints.entity.Trip;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BrandRepository {

    @Inject
    EntityManager em;

    public void save(Brand brand){
        em.persist(brand);
    }

    public Brand findById(Long id){
        return em.find(Brand.class, id);
    }

    public void delete(Long id){
        em.remove(findById(id));
    }

    public List<Brand> getAll(){
        return em.createNamedQuery("Brand.findAll", Brand.class).getResultList();
    }

    public void update(Brand brand){
        Brand br = findById(brand.getId());
        br.setName(brand.getName());

        em.merge(br);
    }
}
