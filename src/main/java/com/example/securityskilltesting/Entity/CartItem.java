package com.example.securityskilltesting.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_item")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CartItem {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable=false)
    private products product;


    private int quantity;


    private Long unitPrice;


}
