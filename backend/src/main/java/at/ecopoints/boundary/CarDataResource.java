package at.ecopoints.boundary;

import at.ecopoints.entity.CarData;
import at.ecopoints.repository.CarDataRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/carData")
public class CarDataResource {
    @Inject
    CarDataRepository carDataRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public CarData getCarDataById(@PathParam("id") Long id) {
        return carDataRepository.findById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CarData> getAllCarData() {
        return carDataRepository.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void saveCarData(CarData carData) {
        carDataRepository.save(carData);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public void deleteCarData(@PathParam("id") Long id) {
        carDataRepository.delete(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public void updateCarData(@PathParam("id") Long id, CarData carData) {
        carDataRepository.update(carData);
    }
}
