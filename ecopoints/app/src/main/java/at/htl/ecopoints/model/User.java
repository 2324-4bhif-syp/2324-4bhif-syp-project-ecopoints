package at.htl.ecopoints.model;

public class User {
    private Long id;
    private String userName;
    private String password;
    private Double ecoPoints;

    public User() {
    }

    public User(Long id, String username, String password, Double ecoPoints) {
        this.id = id;
        this.userName = username;
        this.password = password;
        this.ecoPoints = ecoPoints;
    }

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

    public Double getEcoPoints() {
        return ecoPoints;
    }

    public void setEcoPoints(Double ecoPoints) {
        this.ecoPoints = ecoPoints;
    }
}
