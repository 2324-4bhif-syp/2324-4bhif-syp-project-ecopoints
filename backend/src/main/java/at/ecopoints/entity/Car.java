package at.ecopoints.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ECO_CAR")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Car.findAll",
                        query = "select c from Car c"
                )
        }
)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

}
