package at.htl.ecopoints.util;

import java.time.Duration;

public class DurationParser {
    public static Duration parse(String value) {
        String[] timeParts = value.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        String[] secParts = timeParts[2].split("\\.");
        int seconds = Integer.parseInt(secParts[0]);
        int milliseconds = Integer.parseInt(secParts[1].substring(0, 3));

        Duration duration = Duration.ofHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusMillis(milliseconds);

        return duration;
    }
}
