package com.example.securityskilltesting.Service.IMP;


import com.example.securityskilltesting.Dto.CartDto;
import com.example.securityskilltesting.Entity.Cart;
import com.example.securityskilltesting.Entity.CartItem;
import com.example.securityskilltesting.Entity.UserEntity;
import com.example.securityskilltesting.Entity.products;
import com.example.securityskilltesting.Mapper.CartMapper;
import com.example.securityskilltesting.Repo.CartItemRepo;
import com.example.securityskilltesting.Repo.CartRepo;
import com.example.securityskilltesting.Repo.ProductRepo;
import com.example.securityskilltesting.Repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor

public class CartServices {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;

    @Transactional
    public CartDto addToCart(String username,Long productId, int quantity){
       UserEntity user = userRepo.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("User not found")
       );

       products product= productRepo.findById(productId).orElseThrow(
                ()-> new RuntimeException("Product not found"));

       Cart cart= cartRepo.findByUserEntity(user).orElseGet(
               ()->{
                     Cart newCart= new Cart();
                     newCart.setUserEntity(user);
                     return cartRepo.save(newCart);
               }
       );

       Optional<CartItem> existingItem =cart.getItems().stream()
               .filter(item-> item.getProduct().getId() == productId)
               .findFirst();
       if (existingItem.isPresent()){
              CartItem cartItem= existingItem.get();
              cartItem.setQuantity(cartItem.getQuantity() + quantity);
              cartItemRepo.save(cartItem);
       }
       else{

           CartItem cartItem= new CartItem();
           cartItem.setCart(cart);
           cartItem.setProduct(product);
           cartItem.setQuantity(quantity);
           cartItem.setUnitPrice(product.getPrice());
              cartItemRepo.save(cartItem);
//             cart.getItems().add(cartItem);

       }

Cart updatedCart=cartRepo.findByIdWithItemsAndProducts(cart.getId())
        .orElseThrow(()-> new RuntimeException("Cart not found"));
        return CartMapper.mapToCartDto(updatedCart);
    }
@Transactional
    public CartDto removeFromCart(String username, Long productId){
        UserEntity user=userRepo.findByUsername(username).orElseThrow(
                ()->new RuntimeException("User not found"));
        Cart cart = cartRepo.findByUserEntityWithItemsAndProducts(user)
                .orElseThrow(()->new RuntimeException("Cart not found"));


        cart.getItems().removeIf(item->item.getProduct().getId()==productId);
        return CartMapper.mapToCartDto(cartRepo.save(cart));



}
@Transactional
public  CartDto getCartByUsername(String username){
        UserEntity user=userRepo.findByUsername(username).orElseThrow(
              ()->new RuntimeException("User not found"));
//        Cart cart =cartRepo.findByUserEntity(user)
//                .orElseThrow(()->new RuntimeException("Cart not found"));
                Cart cart= cartRepo.findByUserEntityWithItemsAndProducts(user)
                        .orElseThrow(()-> new RuntimeException("Cart not found"));

        return CartMapper.mapToCartDto(cart);
}

}
