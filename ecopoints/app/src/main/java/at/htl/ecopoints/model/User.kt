package at.htl.ecopoints.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Long?,

    @SerializedName("user_name")
    val userName: String,

    val password: String,

    @SerializedName("eco_points")
    val ecoPoints: Double
) {
    override fun toString(): String {
        return String.format(
            "User[id=%d, userName=%s, password=%s, ecoPoints=%f]",
            id,
            userName,
            password,
            ecoPoints)
    }
}
