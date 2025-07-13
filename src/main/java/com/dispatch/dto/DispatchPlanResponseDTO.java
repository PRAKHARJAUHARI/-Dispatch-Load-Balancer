package com.dispatch.dto;

import com.dispatch.model.DeliveryOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DispatchPlanResponseDTO {
    private String vehicleId;
    private double totalLoad;
    private String totalDistance;
    private List<DeliveryOrder> assignedOrders;
}
