package at.htl.ecopoints.model.dto;

public class EcoPointsMetaData {
    private int HarshAccelerationCount;
    private int HarshBrakingCount;
    private int HighSpeedCount;
    private int HighRpmCount;
    private double AverageEngineLoad;
    private int EcoPoints;

    public EcoPointsMetaData() {
    }

    public EcoPointsMetaData(int harshAccelerationCount, int harshBrakingCount, int highSpeedCount, int highRpmCount, double averageEngineLoad, int ecoPoints) {
        HarshAccelerationCount = harshAccelerationCount;
        HarshBrakingCount = harshBrakingCount;
        HighSpeedCount = highSpeedCount;
        HighRpmCount = highRpmCount;
        AverageEngineLoad = averageEngineLoad;
        EcoPoints = ecoPoints;
    }

    public int getHarshAccelerationCount() {
        return HarshAccelerationCount;
    }

    public void setHarshAccelerationCount(int harshAccelerationCount) {
        HarshAccelerationCount = harshAccelerationCount;
    }

    public int getHarshBrakingCount() {
        return HarshBrakingCount;
    }

    public void setHarshBrakingCount(int harshBrakingCount) {
        HarshBrakingCount = harshBrakingCount;
    }

    public int getHighSpeedCount() {
        return HighSpeedCount;
    }

    public void setHighSpeedCount(int highSpeedCount) {
        HighSpeedCount = highSpeedCount;
    }

    public int getHighRpmCount() {
        return HighRpmCount;
    }

    public void setHighRpmCount(int highRpmCount) {
        HighRpmCount = highRpmCount;
    }

    public double getAverageEngineLoad() {
        return AverageEngineLoad;
    }

    public void setAverageEngineLoad(double averageEngineLoad) {
        AverageEngineLoad = averageEngineLoad;
    }

    public int getEcoPoints() {
        return EcoPoints;
    }

    public void setEcoPoints(int ecoPoints) {
        EcoPoints = ecoPoints;
    }
}
