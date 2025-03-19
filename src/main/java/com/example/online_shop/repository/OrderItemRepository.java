package com.example.online_shop.repository;

import com.example.online_shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Додаткові методи поки що не потрібні
}
