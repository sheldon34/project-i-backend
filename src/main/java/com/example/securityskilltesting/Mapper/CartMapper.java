package com.example.securityskilltesting.Mapper;

import com.example.securityskilltesting.Dto.CartDto;
import com.example.securityskilltesting.Dto.CartItemDto;
import com.example.securityskilltesting.Entity.Cart;
import com.example.securityskilltesting.Entity.CartItem;
import com.example.securityskilltesting.Entity.products;

import java.util.stream.Collectors;
//private products product;
public class CartMapper {
public static CartDto mapToCartDto( Cart cart){
    CartDto cartDto = new CartDto();
    cartDto.setId(cart.getId());
    cartDto.setUserId(Long.valueOf(cart.getUserEntity().getId()));
    cartDto.setCartItems(
            cart.getItems().stream()
                    .map(CartMapper::mapToCartItemDto)
                    .collect(Collectors.toList())
    );
    return cartDto;

}
public static CartItemDto mapToCartItemDto(CartItem item){
    CartItemDto cartItemDto = new CartItemDto();
    cartItemDto.setId(item.getId());
    cartItemDto.setProductId(item.getProduct().getId());
    cartItemDto.setQuantity(Long.valueOf(item.getQuantity()));
    cartItemDto.setUnitPrice(item.getUnitPrice());
    cartItemDto.setProductName(item.getProduct().getName());
    return cartItemDto;
}
}
