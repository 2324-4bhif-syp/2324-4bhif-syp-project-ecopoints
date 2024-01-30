package at.ecopoints.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ECO_BRAND")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Brand.findAll",
                        query = "select b from Brand b"
                )
        }
)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // region Constructors
    public Brand() {}

    public Brand(String name) {
        this.name = name;
    }
    // endregion

    // region Getter and Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    // endregion
}
