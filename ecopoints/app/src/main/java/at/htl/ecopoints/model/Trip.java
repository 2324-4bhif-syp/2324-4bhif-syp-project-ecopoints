package at.htl.ecopoints.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import at.htl.ecopoints.R;

public class Trip {
    private UUID id;
    private Long carId;
    private Long userId;
    private double distance;
    private double avgSpeed;
    private Double avgEngineRotation;
    private Date start;
    private Date end;
    private double rewardedEcoPoints;

    private List<List<CardContent>> detailTripCardContentList;


    public Trip() {
    }

    public Trip(UUID id, Long carId, Long userId, double distance, double avgSpeed, Double avgEngineRotation, Date start, Date end, double rewardedEcoPoints) {
        this.id = id;
        this.carId = carId;
        this.userId = userId;
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.avgEngineRotation = avgEngineRotation;
        this.start = start;
        this.end = end;
        this.rewardedEcoPoints = rewardedEcoPoints;

        this.detailTripCardContentList = new LinkedList<>(){
            {
                add(new LinkedList<>(){
                    {
                        add(new CardContent(String.valueOf(rewardedEcoPoints), "Eco-Points", R.drawable.ranking_category_ecopoints));
                        add(new CardContent(String.valueOf(distance), "Distance", R.drawable.ranking_category_distance));
                    }
                });
                add(new LinkedList<>(){
                    {
                        add(new CardContent(String.valueOf(avgSpeed), "Avg. Speed", R.drawable.ranking_category_avg_speed));
                        add(new CardContent(String.valueOf(avgEngineRotation), "Avg. RPM", R.drawable.ranking_category_avg_rpm));
                    }
                });
            }
        };
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
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

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAvgEngineRotation() {
        return avgEngineRotation;
    }

    public void setAvgEngineRotation(Double avgEngineRotation) {
        this.avgEngineRotation = avgEngineRotation;
    }

    public double getRewardedEcoPoints() {
        return rewardedEcoPoints;
    }

    public void setRewardedEcoPoints(double rewardedEcoPoints) {
        this.rewardedEcoPoints = rewardedEcoPoints;
    }

    public List<List<CardContent>> getDetailTripCardContentList() {
        return detailTripCardContentList;
    }
}
