package at.htl.ecopoints.db.services;

import java.util.List;

import at.htl.ecopoints.model.CarData;
import okhttp3.Response;

public class CarDataService extends Service {
    private final String endPoint = "carData";

    public Response createCarData(CarData carData) throws Exception {
        return super.create(carData, endPoint);
    }

    public Response updateCarData(CarData carData, Long id) throws Exception {
        return super.update(carData, endPoint, id.toString());
    }

    public Response deleteCarData(Long id) throws Exception {
        return super.delete(endPoint, id.toString());
    }

    public CarData getCarDataById(Long id) throws Exception {
        return super.getById(endPoint, id.toString());
    }

    public List<CarData> getAllCarData() throws Exception {
        return super.getAll(endPoint);
    }
}
