package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.User
import org.junit.Before
import org.junit.jupiter.api.Assertions.*

import org.junit.Test
import org.junit.jupiter.api.AfterEach

class UserServiceTest {
    private lateinit var userService: UserService

    @Before
    fun setup() {
        userService = UserService()
    }

    @Test
    fun createUser() {
        // Arrange
        val user = createSampleUser()

        // Act
        val response = userService.createUser(user)

        // Assert
        assertEquals(201, response.code)

    }

    @Test
    fun updateUser() {
        // Arrange
        val user = createSampleUser()

        // Act
        val response = userService.updateUser(user, 1)

        // Assert
        assertEquals(200, response.code)
    }

    @Test
    fun deleteUser() {
        // Arrange

        // Act
        val response = userService.deleteUser(1)

        // Assert
        assertEquals(204, response.code)
    }

    @Test
    fun getUserById() {
        // Arrange

        // Act
        val retrievedUser = userService.getUserById(1)

        // Assert
        assertNotNull(retrievedUser)
    }

    @Test
    fun getAllUsers() {
        // Arrange

        // Act
        val retrievedUsers = userService.getAllUsers()

        // Assert
        assertNotNull(retrievedUsers)
    }

    private fun createSampleUser(): User {
        return User(
            null,
            "Max",
            "Mustermann",
            300.0
        )
    }
}