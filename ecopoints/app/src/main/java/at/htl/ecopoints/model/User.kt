package at.htl.ecopoints.model

data class User(
    val id: Long,
    val userName: String,
    val password: String,
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
