package at.htl.ecopoints.model;

public class Model {
    public String greeting = "1";
    public BtConnectionInfo btConnection = new BtConnectionInfo();
    public CarData carData = new CarData();
    public Trip trip = new Trip();
    public Map map = new Map();
    public RankingInfo rankingInfo = new RankingInfo();

    public HomeInfo homeInfo = new HomeInfo();

    public Model() {
    }
}