package at.ecopoints.entity.DTO;

import at.ecopoints.entity.Trip;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;
import java.util.UUID;

public record CarDataEntry(double longitude,
                           double latitude,
                           @JsonProperty("current_engine_rpm")
                           double currentEngineRPM,
                           @JsonProperty("current_velocity")
                           double currentVelocity,
                           @JsonProperty("throttle_position")
                           double throttlePosition,
                           @JsonProperty("engine_run_time")
                           String engineRunTime,
                           @JsonProperty("time_stamp")
                           Timestamp timeStamp,
                           @JsonProperty("trip_id")
                           UUID tripId
){}

