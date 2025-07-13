package com.dispatch.service;

import com.dispatch.model.DeliveryOrder;
import com.dispatch.model.Priority;
import com.dispatch.model.Vehicle;
import com.dispatch.repository.OrderRepository;
import com.dispatch.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceTest {

    private OrderRepository orderRepo;
    private VehicleRepository vehicleRepo;
    private DispatchService dispatchService;

    @BeforeEach
    void setUp() {
        orderRepo = mock(OrderRepository.class);
        vehicleRepo = mock(VehicleRepository.class);
        dispatchService = new DispatchService(orderRepo, vehicleRepo);
    }

    @Test
    void testGenerateDispatchPlan_AssignsOrdersCorrectly() {
        DeliveryOrder order1 = new DeliveryOrder("ORD001", Priority.HIGH, 15.0, "CP, Delhi", 77.2090, 28.6139);
        DeliveryOrder order2 = new DeliveryOrder("ORD002", Priority.MEDIUM, 20.0, "Noida", 77.3910, 28.5355);

        Vehicle vehicle1 = new Vehicle("VEH001", 100.0, 28.7041, 77.1025, "Karol Bagh");
        Vehicle vehicle2 = new Vehicle("VEH002", 50.0, 28.4595, 77.0266, "Gurgaon");

        when(orderRepo.findAll()).thenReturn(List.of(order1, order2));
        when(vehicleRepo.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        var result = dispatchService.generateDispatchPlan();

        assertEquals(2, result.size());
        double totalLoad = result.stream().mapToDouble(r -> r.getTotalLoad()).sum();
        assertEquals(35.0, totalLoad);
    }

    @Test
    void testGenerateDispatchPlan_EmptyOrders() {
        when(orderRepo.findAll()).thenReturn(List.of());
        when(vehicleRepo.findAll()).thenReturn(List.of(new Vehicle("VEH001", 100.0, 28.6, 77.2, "CP")));

        var result = dispatchService.generateDispatchPlan();
        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).getTotalLoad());
    }
}
