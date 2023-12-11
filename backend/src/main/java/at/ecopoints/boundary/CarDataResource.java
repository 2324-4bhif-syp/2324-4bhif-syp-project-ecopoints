package at.ecopoints.boundary;

import at.ecopoints.entity.CarData;
import at.ecopoints.entity.DTO.CarDataEntry;
import at.ecopoints.repository.CarDataRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response saveCarData(CarDataEntry carDataEntry) {
        carDataRepository.save(carDataEntry);
        return Response.status(Response.Status.CREATED).entity("CarData created successfully").build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response deleteCarData(@PathParam("id") Long id) {
        carDataRepository.delete(id);
        return Response.status(Response.Status.NO_CONTENT).entity("CarData removed successfully").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response updateCarData(@PathParam("id") Long id, CarDataEntry carDataEntry) {
        carDataRepository.update(carDataEntry, id);
        return Response.status(Response.Status.OK).entity("CarData updated successfully").build();
    }
}
