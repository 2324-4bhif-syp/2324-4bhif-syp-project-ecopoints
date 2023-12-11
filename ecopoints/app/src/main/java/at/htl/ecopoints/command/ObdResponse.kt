package at.htl.ecopoints.command

data class ObdResponse(
    val command: ObdCommand,
    val rawResponse: ObdRawResponse,
    val value: String,
    val unit: String = ""
) {
    val formattedValue: String get() = ""//command.format(this)
}