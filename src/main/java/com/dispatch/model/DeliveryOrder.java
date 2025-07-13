package com.dispatch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class DeliveryOrder {
    @Id
    private String orderId;

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private double latitude;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private double longitude;
    private String address;
    private double packageWeight;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    public DeliveryOrder() {
        // Required by JPA/Hibernate
    }

    public DeliveryOrder(String orderId, Priority priority, double packageWeight, String address, double longitude, double latitude) {
        this.orderId = orderId;
        this.priority = priority;
        this.packageWeight = packageWeight;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
