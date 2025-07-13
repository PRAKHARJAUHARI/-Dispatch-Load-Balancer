package com.dispatch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Vehicle {

    @Id
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Double capacity;

    @NotNull(message = "Current latitude is required")
    private Double currentLatitude;

    @NotNull(message = "Current longitude is required")
    private Double currentLongitude;

    @NotBlank(message = "Current address is required")
    private String currentAddress;

    public Vehicle() {
        // Hibernate needs this
    }

    public Vehicle(String vehicleId, Double capacity, Double currentLatitude, Double currentLongitude, String currentAddress) {
        this.vehicleId = vehicleId;
        this.capacity = capacity;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.currentAddress = currentAddress;
    }

}
