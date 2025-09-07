package com.example.securityskilltesting.Dto;

import com.example.securityskilltesting.Entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
//    private
    private Long id;
   private Long userId;
    private List<CartItemDto> cartItems;


}
