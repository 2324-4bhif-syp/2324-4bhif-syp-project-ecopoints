package at.htl.ecopoints.model;

public class User {
    private Long id;
    private String username;
    private String password;
    private Double ecoPoints;

    public User() {
    }

    public User(Long id, String username, String password, Double ecoPoints) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.ecoPoints = ecoPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
