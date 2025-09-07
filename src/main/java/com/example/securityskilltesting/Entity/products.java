package com.example.securityskilltesting.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Students")
public class products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private Long price;
    private String Quantity;
    private String description;
    @Lob
    @Basic (fetch=FetchType.LAZY)
    private byte[] image;

    @OneToMany(mappedBy = "product",cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems=new ArrayList<>();



}
