package at.ecopoints.entity;

public class User {
    private Long id;
    private String userName;
    private String password;
    private double ecoPoints;
    
    // region Constructors
    public User() { }

    public User(String userName, String password, double ecoPoints) {
        this.userName = userName;
        this.password = password;
        this.ecoPoints = ecoPoints;
    }
    // endregion

    // region Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getEcoPoints() {
        return ecoPoints;
    }

    public void setEcoPoints(double ecoPoints) {
        this.ecoPoints = ecoPoints;
    }
    // endregion
}
