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

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.REMOVE
    })
    private Brand brand;

    //region Constructors
    public Car(){}

    public Car(String model, Brand brand) {
        this.model = model;
        this.brand = brand;
    }
    //endregion

    //region Getter and Setter
    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    //endregion
}
