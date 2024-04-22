package at.htl.ecopoints.model;

import java.util.LinkedList;
import java.util.List;

import at.htl.ecopoints.R;

public class User {
    private Long id;
    private String userName;
    private String password;
    private Double ecoPoints;

    private List<List<DetailRankingCardContent>> detailRankingCardContentList;

    public User() {
    }

    public User(Long id, String username, String password, Double ecoPoints) {
        this.id = id;
        this.userName = username;
        this.password = password;
        this.ecoPoints = ecoPoints;



        this.detailRankingCardContentList = new LinkedList<>(){
            {
                add(new LinkedList<>(){
                    {
                        add(new DetailRankingCardContent(ecoPoints.toString(), "Eco-Points", R.drawable.ranking_category_ecopoints));
                        add(new DetailRankingCardContent("14,2 kg", "CO₂-Consumption", R.drawable.ranking_category_co2));
                    }
                });
                add(new LinkedList<>(){
                    {
                        add(new DetailRankingCardContent("5", "Trips", R.drawable.ranking_category_trips));
                        add(new DetailRankingCardContent("100 km", "Distance", R.drawable.ranking_category_distance));
                    }
                });
                add(new LinkedList<>(){
                    {
                        add(new DetailRankingCardContent("2", "Cars", R.drawable.ranking_category_cars));
                        add(new DetailRankingCardContent("Diesel", "Fuel Type", R.drawable.ranking_category_fuel_type));
                    }
                });
                add(new LinkedList<>(){
                    {
                        add(new DetailRankingCardContent("41 km/h", "Avg. Speed", R.drawable.ranking_category_avg_speed));
                        add(new DetailRankingCardContent("2000", "Avg. RPM", R.drawable.ranking_category_avg_rpm));
                    }
                });
            }
        };
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

    public List<List<DetailRankingCardContent>> getDetailRankingCardContentList() {
        return detailRankingCardContentList;
    }
}
