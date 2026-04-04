package com.seproject.inventory.repository;

import com.seproject.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.seller WHERE p.seller.id = :sellerId")
	List<Product> findBySellerId(@Param("sellerId") Long sellerId);
}
