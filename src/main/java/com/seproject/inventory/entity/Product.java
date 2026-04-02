package com.seproject.inventory.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private int quantity;

    private double price;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Transient
    @JsonProperty("stock")
    public int getStock() {
        return this.quantity;
    }

    @Transient
    @JsonProperty("sellerId")
    public Long getSellerId() {
        return this.seller != null ? this.seller.getId() : null;
    }
}
