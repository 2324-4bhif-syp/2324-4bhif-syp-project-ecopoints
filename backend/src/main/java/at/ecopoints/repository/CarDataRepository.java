package at.ecopoints.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class CarDataRepository {

    @Inject
    EntityManager em;
}
