package com.seproject.inventory.repository;

import com.seproject.inventory.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("SELECT DISTINCT o FROM Order o " +
	       "LEFT JOIN FETCH o.buyer " +
	       "LEFT JOIN FETCH o.product p " +
	       "LEFT JOIN FETCH p.seller " +
	       "WHERE o.buyer.id = :buyerId " +
	       "ORDER BY o.createdAt DESC")
	List<Order> findByBuyerId(@Param("buyerId") Long buyerId);

	@Query("SELECT DISTINCT o FROM Order o " +
	       "LEFT JOIN FETCH o.buyer " +
	       "LEFT JOIN FETCH o.product p " +
	       "LEFT JOIN FETCH p.seller " +
	       "WHERE p.seller.id = :sellerId " +
	       "ORDER BY o.createdAt DESC")
	List<Order> findBySellerIdOrderByCreatedAtDesc(@Param("sellerId") Long sellerId);
}
