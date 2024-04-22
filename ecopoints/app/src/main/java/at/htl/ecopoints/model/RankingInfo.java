package at.htl.ecopoints.model;

import androidx.compose.ui.graphics.painter.Painter;
import androidx.compose.ui.res.PainterResources_androidKt;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import at.htl.ecopoints.R;

public class RankingInfo {
    public User selectedUser = new User();
    public boolean showDetailRankingView = false;
    public List<User> users = new LinkedList<>();
    public List<FuelType> fuelTypes = new LinkedList<>(){
        {
            add(new FuelType("Diesel"));
            add(new FuelType("Petrol"));
        }
    };
    public List<FuelType> selectedFuelTypes = new LinkedList<>(){
        {
            add(fuelTypes.get(0));
            add(fuelTypes.get(1));
        }
    };
    public boolean showFuelTypeDropdown = false;
    public HashMap<User, Integer> ranks = new HashMap<>();
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
