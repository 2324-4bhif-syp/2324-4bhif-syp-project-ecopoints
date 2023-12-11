package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.User
import okhttp3.Response

class UserService: Service() {
    private final val endPoint = "user"

    fun createUser(user: User): Response {
        return super.create(user, endPoint)
    }

    fun updateUser(user: User, id: Long): Response {
        return super.update(user, endPoint, id.toString())
    }

    fun deleteUser(id: Long): Response {
        return super.delete(endPoint, id.toString())
    }

    fun getUserById(id: Long): User? {
        return super.getById<User>(endPoint, id.toString())
    }

    fun getAllUsers(): List<User>? {
        return super.getAll<User>(endPoint)
    }
}