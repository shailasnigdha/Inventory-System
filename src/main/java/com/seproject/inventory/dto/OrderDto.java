package com.seproject.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Long productId;
    private int quantity;
}
