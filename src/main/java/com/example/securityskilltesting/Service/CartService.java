package com.example.securityskilltesting.Service;


import com.example.securityskilltesting.Entity.Cart;
import com.example.securityskilltesting.Entity.UserEntity;
import com.example.securityskilltesting.Entity.CartItem;
import com.example.securityskilltesting.Entity.products;
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

public class CartService {
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;
    private final UserRepo userRepo;

    @Transactional
    public Cart addToCart(UserEntity user, Long productId, int  quantity){

        products product= productRepo.findById(productId)
                .orElseThrow(()->new RuntimeException("product not found"));
      UserEntity validuser = userRepo.findById(user.getId()).orElseThrow(()->new RuntimeException("User not found"));


        Cart cart = cartRepo.findByUserEntity(validuser)
                .orElseGet(()->{
                    Cart newCart= new Cart();
                    newCart.setUserEntity( validuser);
                    return cartRepo.save(newCart);
                });

        Optional<CartItem>  existingItem=cart.getItems().stream()
                .filter(item->item.getProduct().getId()==productId)
                .findFirst();
        if (existingItem.isPresent()){
            CartItem cartItem=existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
           cartItemRepo.save(cartItem);
        }else{
            CartItem cartItem=new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            cart.getItems().add(cartItem);

        }

        return  cartRepo.save(cart);

    }

    @Transactional
    public Cart removeFromCart(UserEntity user, Long productId){
        Cart cart =cartRepo.findByUserEntity(user)
                .orElseThrow(()->new RuntimeException("Cart not found for user"));
        cart.getItems().removeIf(item->item.getProduct().getId() == productId);
        return cartRepo.save(cart);

    }
public Cart getCart(UserEntity user){
    return cartRepo.findByUserEntity(user)
            .orElseThrow(()->new RuntimeException("Cart not found for user"));
}

}
