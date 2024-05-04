package at.htl.ecopoints.model;

import com.google.android.gms.maps.model.LatLng;
import androidx.compose.ui.graphics.Color;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Map {
    public boolean showMap = false;
    public List<Pair<Color, Pair<LatLng, Double>>> latLngList = new ArrayList<>();
}
