package at.htl.ecopoints.command

import com.github.eltonvs.obd.command.RegexPatterns
import com.github.eltonvs.obd.command.pipe

data class ObdRawResponse(
    val value: String,
    val elapsedTime: Long
) {
    private val valueProcessorPipeline by lazy {
        arrayOf<(String) -> String>(
            {
                removeAll(
                    RegexPatterns.WHITESPACE_PATTERN,
                    it
                )
            },
            {

                removeAll(RegexPatterns.BUS_INIT_PATTERN, it)
            },
            {
                removeAll(RegexPatterns.COLON_PATTERN, it)
            }
        )
    }

    val processedValue by lazy { value.pipe(*valueProcessorPipeline) }

    val bufferedValue by lazy { processedValue.chunked(2) { it.toString().toInt(radix = 16) }.toIntArray() }
}
