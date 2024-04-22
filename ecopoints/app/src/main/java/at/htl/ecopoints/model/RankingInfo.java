package at.htl.ecopoints.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import at.htl.ecopoints.R;

public class RankingInfo {
    public User selectedUser = new User();
    public boolean showDetailRankingView = false;
    public List<User> users = new LinkedList<>(){
        {
            add(new User(1L, "Joe", "123", 547.1));
            add(new User(2L, "Mary", "123", 533.9));
            add(new User(3L, "Chris", "123", 513.4));
            add(new User(4L, "John", "123", 431.3));
            add(new User(5L, "Hary", "123", 347.1));
            add(new User(6L, "Jane", "123", 333.9));
            add(new User(7L, "Max", "123", 313.4));
            add(new User(8L, "Mike", "123", 231.3));
            add(new User(9L, "Chloe", "123", 133.9));
            add(new User(10L, "Courtney", "123", 113.4));
            add(new User(11L, "Lisa", "123", 91.3));
        }
    };
    public List<FuelType> fuelTypes = new LinkedList<>(){
        {
            add(new FuelType("Diesel", true));
            add(new FuelType("Petrol", true));
        }
    };
    public List<FuelType> selectedFuelTypes = new LinkedList<>(){
        {
            add(fuelTypes.get(0));
            add(fuelTypes.get(1));
        }
    };
    public boolean showFuelTypeDropdown = false;

    public List<Integer> ranks = new LinkedList<>(){
        {
            add(R.drawable.ranking_place_1);
            add(R.drawable.ranking_place_2);
            add(R.drawable.ranking_place_3);
        }
    };
    public HashMap<String, Integer> rankTypeOptions = new HashMap<>(){
        {
            put("CO₂-Consumption", R.drawable.ranking_category_co2);
            put("Eco-Points", R.drawable.ranking_category_ecopoints);
        }
    };
    public String selectedRankTypeOption = rankTypeOptions.keySet().stream().findFirst().get();

    public RankingInfo() {

    }
}
