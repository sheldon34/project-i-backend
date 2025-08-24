package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Entity.Cart;
import com.example.securityskilltesting.Entity.UserEntity;
import com.example.securityskilltesting.Repo.UserRepo;
import com.example.securityskilltesting.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor

public class CartController {
    private final CartService cartService;
    private final UserRepo userRepo;

    @PostMapping("/add")
    public Cart addToCart(

            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestParam Long productId,
            @RequestParam int quantity
    ){


        if (userDetails == null) {
            throw new RuntimeException("User is not authenticated");
        }


        UserEntity validuser = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
      return   cartService.addToCart(validuser, productId, quantity);
    }
    @DeleteMapping("/remove")
    public Cart removeFromCart(
            @AuthenticationPrincipal UserEntity user ,
            @RequestParam Long productId
    ){
        return  cartService.removeFromCart(user,productId);
    }
@GetMapping
    public Cart viewCart(@AuthenticationPrincipal UserEntity user){
    return cartService.getCart(user);
    }



}
