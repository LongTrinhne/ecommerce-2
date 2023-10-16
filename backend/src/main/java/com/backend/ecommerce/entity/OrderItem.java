package com.backend.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private Long price;

    @Column(name = "discounted_price")
    private Long discountedPrice;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
}
