package at.ecopoints.boundary;

import at.ecopoints.entity.Car;
import at.ecopoints.repository.CarRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/car")
public class CarResource {

    @Inject
    CarRepository carRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Car getCarById(@PathParam("id") Long id) {
        return carRepository.findById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Car> getAllCars() {
        return carRepository.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveCar(Car car) {
        carRepository.save(car);
        return Response.status(Response.Status.CREATED).entity("Car created successfully").build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response deleteCar(@PathParam("id") Long id) {
        carRepository.delete(id);
        return Response.status(Response.Status.NO_CONTENT).entity("Car removed successfully").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response updateCar(@PathParam("id") Long id, Car car) {
        carRepository.update(car);
        return Response.status(Response.Status.OK).entity("Car updated successfully").build();
    }
}
