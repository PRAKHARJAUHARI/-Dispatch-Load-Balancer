package com.dispatch.repository;

import com.dispatch.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<DeliveryOrder, String> {
}
