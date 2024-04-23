package at.htl.ecopoints.db.services;
import java.util.List;

import at.htl.ecopoints.model.User;
import okhttp3.Response;

public class UserService extends Service {
    private final String endPoint = "user";

    public Response createUser(User user) throws Exception {
        return super.create(user, endPoint);
    }

    public Response updateUser(User user, Long id) throws Exception {
        return super.update(user, endPoint, id.toString());
    }

    public Response deleteUser(Long id) throws Exception {
        return super.delete(endPoint, id.toString());
    }

    public User getUserById(Long id) throws Exception {
        return super.getById(endPoint, id.toString());
    }

    public List<User> getAllUsers() throws Exception {
        return super.getAll(endPoint);
    }
}
