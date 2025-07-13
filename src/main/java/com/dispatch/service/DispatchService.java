package com.dispatch.service;

import com.dispatch.dto.DispatchPlanResponseDTO;
import com.dispatch.model.DeliveryOrder;
import com.dispatch.model.Vehicle;
import com.dispatch.repository.OrderRepository;
import com.dispatch.repository.VehicleRepository;
import com.dispatch.util.HaversineCalculator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DispatchService {

    private final OrderRepository orderRepo;
    private final VehicleRepository vehicleRepo;

    public DispatchService(OrderRepository orderRepo, VehicleRepository vehicleRepo) {
        this.orderRepo = orderRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public void addOrders(List<DeliveryOrder> orders) {
        orderRepo.saveAll(orders);
    }

    public void addVehicles(List<Vehicle> vehicles) {
        vehicleRepo.saveAll(vehicles);
    }

    public List<DispatchPlanResponseDTO> generateDispatchPlan() {
        List<DeliveryOrder> orders = orderRepo.findAll();
        List<Vehicle> vehicles = vehicleRepo.findAll();

        // Sort by priority (HIGH → LOW)
        List<DeliveryOrder> mutableOrders = new ArrayList<>(orders);
        mutableOrders.sort(Comparator.comparing(DeliveryOrder::getPriority));

        // Map to track assignments and remaining capacity
        Map<String, List<DeliveryOrder>> assignment = new HashMap<>();
        Map<String, Double> remainingCapacity = new HashMap<>();

        for (Vehicle v : vehicles) {
            assignment.put(v.getVehicleId(), new ArrayList<>());
            remainingCapacity.put(v.getVehicleId(), v.getCapacity());
        }

        // Assign orders to best-fit vehicle
        for (DeliveryOrder order : orders) {
            Vehicle bestVehicle = null;
            double minDistance = Double.MAX_VALUE;

            for (Vehicle v : vehicles) {
                if (remainingCapacity.get(v.getVehicleId()) >= order.getPackageWeight()) {
                    List<DeliveryOrder> assignedOrders = assignment.get(v.getVehicleId());

                    double lastLat = assignedOrders.isEmpty() ? v.getCurrentLatitude()
                            : assignedOrders.get(assignedOrders.size() - 1).getLatitude();
                    double lastLon = assignedOrders.isEmpty() ? v.getCurrentLongitude()
                            : assignedOrders.get(assignedOrders.size() - 1).getLongitude();

                    double distance = HaversineCalculator.calculate(lastLat, lastLon,
                            order.getLatitude(), order.getLongitude());

                    if (distance < minDistance) {
                        bestVehicle = v;
                        minDistance = distance;
                    }
                }
            }

            if (bestVehicle != null) {
                assignment.get(bestVehicle.getVehicleId()).add(order);
                double updatedCapacity = remainingCapacity.get(bestVehicle.getVehicleId()) - order.getPackageWeight();
                remainingCapacity.put(bestVehicle.getVehicleId(), updatedCapacity);
            }
        }

        // Build response DTOs
        List<DispatchPlanResponseDTO> response = new ArrayList<>();

        for (Vehicle v : vehicles) {
            List<DeliveryOrder> assigned = assignment.get(v.getVehicleId());
            double load = assigned.stream().mapToDouble(DeliveryOrder::getPackageWeight).sum();
            double totalDistance = calculateTotalDistance(v, assigned);

            response.add(new DispatchPlanResponseDTO(
                    v.getVehicleId(),
                    load,
                    String.format("%.2f km", totalDistance),
                    assigned
            ));
        }

        return response;
    }

    private double calculateTotalDistance(Vehicle vehicle, List<DeliveryOrder> orders) {
        if (orders == null || orders.isEmpty()) return 0.0;

        double total = 0.0;
        double lastLat = vehicle.getCurrentLatitude();
        double lastLon = vehicle.getCurrentLongitude();

        for (DeliveryOrder order : orders) {
            double dist = HaversineCalculator.calculate(lastLat, lastLon,
                    order.getLatitude(), order.getLongitude());
            total += dist;
            lastLat = order.getLatitude();
            lastLon = order.getLongitude();
        }

        return total;
    }
}
