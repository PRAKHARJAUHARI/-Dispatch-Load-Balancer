package com.dispatch.controller;

import com.dispatch.dto.DispatchPlanResponseDTO;
import com.dispatch.model.DeliveryOrder;
import com.dispatch.model.Vehicle;
import com.dispatch.service.DispatchService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    private static final Logger logger = LoggerFactory.getLogger(DispatchController.class);

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> addOrders(@Valid @RequestBody Map<String, List<DeliveryOrder>> body) {
        try {
            List<DeliveryOrder> orders = body.get("orders");
            if (orders == null || orders.isEmpty()) {
                logger.warn("No orders provided in request body.");
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No delivery orders provided.",
                        "status", "failed"
                ));
            }

            dispatchService.addOrders(orders);
            return ResponseEntity.ok(Map.of(
                    "message", "Delivery orders accepted.",
                    "status", "success"
            ));

        } catch (Exception ex) {
            logger.error("Error processing orders", ex);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to process delivery orders.",
                    "details", ex.getMessage()
            ));
        }
    }

    @PostMapping("/vehicles")
    public ResponseEntity<?> addVehicles(@Valid @RequestBody Map<String, List<Vehicle>> body) {
        try {
            List<Vehicle> vehicles = body.get("vehicles");
            if (vehicles == null || vehicles.isEmpty()) {
                logger.warn("No vehicles provided in request body.");
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No vehicle details provided.",
                        "status", "failed"
                ));
            }

            dispatchService.addVehicles(vehicles);
            return ResponseEntity.ok(Map.of(
                    "message", "Vehicle details accepted.",
                    "status", "success"
            ));

        } catch (Exception ex) {
            logger.error("Error processing vehicles", ex);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to process vehicle data.",
                    "details", ex.getMessage()
            ));
        }
    }

    @GetMapping("/plan")
    public ResponseEntity<?> getDispatchPlan() {
        try {
            List<DispatchPlanResponseDTO> plan = dispatchService.generateDispatchPlan();
            if (plan == null || plan.isEmpty()) {
                logger.warn("Dispatch plan is empty or could not be generated.");
                return ResponseEntity.ok(Map.of(
                        "message", "No dispatch plan generated.",
                        "dispatchPlan", List.of()
                ));
            }

            return ResponseEntity.ok(Map.of("dispatchPlan", plan));
        } catch (Exception ex) {
            logger.error("Error generating dispatch plan", ex);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to generate dispatch plan.",
                    "details", ex.getMessage()
            ));
        }
    }
}
