package at.ecopoints.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ECO_TRIP")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Trip.findAll",
                        query = "select t from Trip t"
                ),
                @NamedQuery(
                        name = "Trip.findByUserId",
                        query = "select t from Trip t where t.user.id = :userId"
                )
        }
)
public class Trip {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double distance;
    private double avgSpeed;
    private double avgEngineRotation;
    private Date date;
    private double rewardedEcoPoints;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    private User user;

    // region Constructors
    public Trip() {}

    public Trip(double distance, double avgSpeed, double avgEngineRotation, Date date, double rewardedEcoPoints, User user) {
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.avgEngineRotation = avgEngineRotation;
        this.date = date;
        this.rewardedEcoPoints = rewardedEcoPoints;
        this.user = user;
    }
    // endregion
    
    // region Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgEngineRotation() {
        return avgEngineRotation;
    }

    public void setAvgEngineRotation(double avgEngineRotation) {
        this.avgEngineRotation = avgEngineRotation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRewardedEcoPoints() {
        return rewardedEcoPoints;
    }

    public void setRewardedEcoPoints(double rewardedEcoPoints) {
        this.rewardedEcoPoints = rewardedEcoPoints;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // endregion
}
