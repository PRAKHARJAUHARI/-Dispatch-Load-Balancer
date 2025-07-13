package com.dispatch.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DeliveryOrderTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDeliveryOrder() {
        DeliveryOrder order = new DeliveryOrder("O-123", Priority.HIGH, 5.0, "123 Main St", 77.5, 13.1);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertTrue(violations.isEmpty(), "There should be no validation errors for valid input");
    }

    @Test
    public void testLatitudeOutOfBoundsLow() {
        DeliveryOrder order = new DeliveryOrder("O-001", Priority.LOW, 2.5, "Bad Location", 80.0, -91.0);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("latitude")));
    }

    @Test
    public void testLatitudeOutOfBoundsHigh() {
        DeliveryOrder order = new DeliveryOrder("O-002", Priority.LOW, 2.5, "Too North", 80.0, 91.0);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("latitude")));
    }

    @Test
    public void testLongitudeOutOfBoundsLow() {
        DeliveryOrder order = new DeliveryOrder("O-003", Priority.MEDIUM, 3.0, "Too West", -181.0, 20.0);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("longitude")));
    }

    @Test
    public void testLongitudeOutOfBoundsHigh() {
        DeliveryOrder order = new DeliveryOrder("O-004", Priority.MEDIUM, 3.0, "Too East", 181.0, 20.0);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("longitude")));
    }

    @Test
    public void testNullLatitude() {
        DeliveryOrder order = new DeliveryOrder("O-005", Priority.HIGH, 1.0, "Missing lat", 75.0, 0.0);
        order.setLatitude(0.0); // not null, but check if validation still passes
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertTrue(violations.isEmpty(), "Zero latitude should be allowed");
    }

    @Test
    public void testNullLongitude() {
        DeliveryOrder order = new DeliveryOrder("O-006", Priority.HIGH, 1.0, "Missing long", 0.0, 20.0);
        order.setLongitude(0.0); // zero is valid
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertTrue(violations.isEmpty(), "Zero longitude should be allowed");
    }

    @Test
    public void testNullPriority() {
        DeliveryOrder order = new DeliveryOrder("O-007", null, 1.0, "No priority", 75.0, 20.0);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        // Since priority is not annotated with @NotNull, this will pass unless you change the model
        assertTrue(violations.isEmpty(), "Null priority is currently allowed");
    }

    @Test
    public void testExtremeValidCoordinates() {
        DeliveryOrder order = new DeliveryOrder("O-008", Priority.HIGH, 2.0, "Edge coordinates", 180.0, -90.0);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        assertTrue(violations.isEmpty(), "Edge values should be valid");
    }

    @Test
    public void testNegativePackageWeight() {
        DeliveryOrder order = new DeliveryOrder("O-009", Priority.HIGH, -5.0, "Negative weight", 100.0, 45.0);
        Set<ConstraintViolation<DeliveryOrder>> violations = validator.validate(order);
        // No @DecimalMin on weight, so this passes unless you add constraint
        assertTrue(violations.isEmpty(), "Negative weight is allowed unless validated explicitly");
    }
}
