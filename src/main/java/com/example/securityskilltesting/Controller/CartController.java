package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.CartDto;
import com.example.securityskilltesting.Entity.Cart;
import com.example.securityskilltesting.Entity.UserEntity;
import com.example.securityskilltesting.Repo.UserRepo;
//import com.example.securityskilltesting.Service.;
import com.example.securityskilltesting.Service.IMP.CartServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor

public class CartController {
    private final CartServices cartService;
    private final UserRepo userRepo;



    @PostMapping("/add")
    public ResponseEntity<CartDto>   addToCart(
            Authentication auth,
            @RequestParam Long productId,
            @RequestParam int quantity
    ){
        String username= auth.getName();
        return ResponseEntity.ok(cartService.addToCart(username,productId,quantity));


    }
    @DeleteMapping("/remove")
    public ResponseEntity<CartDto> removeFromCart(
            Authentication auth,
            @RequestParam Long productId
    ){
      String username= auth.getName();
      return ResponseEntity.ok(cartService.removeFromCart(username,productId));

    }
@GetMapping("/get")
    public ResponseEntity<CartDto> getCart(Authentication auth){
    String username= auth.getName();
    return ResponseEntity.ok(cartService.getCartByUsername(username));
}
}
