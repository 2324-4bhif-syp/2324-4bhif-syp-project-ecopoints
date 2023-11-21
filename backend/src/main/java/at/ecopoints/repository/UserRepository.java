package at.ecopoints.repository;

import at.ecopoints.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserRepository {
    @Inject
    EntityManager em;
<<<<<<< HEAD
=======

    @Transactional
>>>>>>> 06114ffd9a2a7375b55b2e273e9d742316d1fc34
    public void save(User user){
        em.persist(user);
    }
    public User findById(Long id){
        return em.find(User.class, id);
    }
    @Transactional
    public void delete(Long id){
        em.remove(findById(id));
    }
    public List<User> getAll(){
        return em.createQuery("select u from User u", User.class).getResultList();
    }
    @Transactional
    public void update(User user){
        User newUser = findById(user.getId());

        newUser.setId(user.getId());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setEcoPoints(user.getEcoPoints());

        em.merge(newUser);
    }
}
