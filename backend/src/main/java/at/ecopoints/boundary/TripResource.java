package at.ecopoints.boundary;

import at.ecopoints.entity.Trip;
import at.ecopoints.repository.TripRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/trip")
public class TripResource {
    @Inject
    TripRepository tripRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Trip getCarDataById(@PathParam("id") Long id) {
        return tripRepository.findById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trip> getAllCarData() {
        return tripRepository.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveCarData(Trip trip) {
        tripRepository.save(trip);
        return Response.status(Response.Status.CREATED).entity("Trip created successfully").build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response deleteCarData(@PathParam("id") Long id) {
        tripRepository.delete(id);
        return Response.status(Response.Status.OK).entity("Trip removed successfully").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response updateCarData(@PathParam("id") Long id, Trip trip) {
        tripRepository.update(trip);
        return Response.status(Response.Status.OK).entity("Trip updated successfully").build();
    }
}
