package com.dispatch.controller;

import com.dispatch.dto.DispatchPlanResponseDTO;
import com.dispatch.model.DeliveryOrder;
import com.dispatch.model.Priority;
import com.dispatch.model.Vehicle;
import com.dispatch.service.DispatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DispatchController.class)
class DispatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DispatchService dispatchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrders_ReturnsSuccess() throws Exception {
        DeliveryOrder order = new DeliveryOrder("ORD001", Priority.HIGH, 77.2, "CP", 10, 28.6);
        Map<String, List<DeliveryOrder>> payload = Map.of("orders", List.of(order));

        mockMvc.perform(post("/api/dispatch/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testAddVehicles_ReturnsSuccess() throws Exception {
        Vehicle vehicle = new Vehicle("VEH001", 100.0, 28.6, 77.2, "CP");
        Map<String, List<Vehicle>> payload = Map.of("vehicles", List.of(vehicle));

        mockMvc.perform(post("/api/dispatch/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testGetDispatchPlan_ReturnsDispatchList() throws Exception {
        DispatchPlanResponseDTO dto = new DispatchPlanResponseDTO("VEH001", 10, "5.00 km", List.of());
        Mockito.when(dispatchService.generateDispatchPlan()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/dispatch/plan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dispatchPlan[0].vehicleId").value("VEH001"));
    }
}
