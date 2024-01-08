package at.ecopoints.boundary;

import at.ecopoints.entity.Brand;
import at.ecopoints.repository.BrandRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/brand")
public class BrandResource {

    @Inject
    BrandRepository brandRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Brand getBrandById(@PathParam("id") Long id) {
        return brandRepository.findById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Brand> getAllBrands() {
        return brandRepository.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveBrand(Brand brand) {
        brandRepository.save(brand);
        return Response.status(Response.Status.CREATED).entity("Brand created successfully").build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response deleteBrand(@PathParam("id") Long id) {
        brandRepository.delete(id);
        return Response.status(Response.Status.NO_CONTENT).entity("Brand removed successfully").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response updateBrand(@PathParam("id") Long id, Brand brand) {
        brandRepository.update(brand);
        return Response.status(Response.Status.OK).entity("Brand updated successfully").build();
    }
}
