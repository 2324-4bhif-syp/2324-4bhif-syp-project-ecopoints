package at.ecopoints.boundary;

import at.ecopoints.entity.User;
import at.ecopoints.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserResource {
    @Inject
    UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public User getCarDataById(@PathParam("id") Long id) {
        return userRepository.findById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllCarData() {
        return userRepository.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveCarData(User user) {
        userRepository.save(user);
        return Response.status(Response.Status.CREATED).entity("User created successfully").build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response deleteCarData(@PathParam("id") Long id) {
        userRepository.delete(id);
        return Response.status(Response.Status.OK).entity("User removed successfully").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response updateCarData(@PathParam("id") Long id, User user) {
        userRepository.update(user);
        return Response.status(Response.Status.OK).entity("User updated successfully").build();
    }
}
