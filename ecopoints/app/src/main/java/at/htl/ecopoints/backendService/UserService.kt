package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.User

class UserService: Service() {
    private final val endPoint = "user"

    fun createUser(user: User) {
        super.create(user, endPoint)
    }

    fun updateUser(user: User, id: Long) {
        super.update(user, endPoint, id)
    }

    fun deleteUser(id: Long) {
        super.delete(endPoint, id)
    }

    fun getUserById(id: Long): User? {
        return super.getById<User>(endPoint, id)
    }

    fun getAllUsers(): List<User>? {
        return super.getAll<User>(endPoint)
    }
}