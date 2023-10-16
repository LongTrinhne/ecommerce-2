package com.backend.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @CreationTimestamp
    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    @OneToOne
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonIgnore
    private List<OrderItem> orderItems;

    private Long totalPrice;

    private Long totalDiscountedPrice;

    private Long discount;

    private String orderStatus;

    private Integer totalItems;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
