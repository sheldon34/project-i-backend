//package com.example.securityskilltesting.Service;
//
//
//import com.example.securityskilltesting.Entity.Cart;
//import com.example.securityskilltesting.Entity.UserEntity;
//import com.example.securityskilltesting.Entity.CartItem;
//import com.example.securityskilltesting.Entity.products;
//import com.example.securityskilltesting.Repo.CartItemRepo;
//import com.example.securityskilltesting.Repo.CartRepo;
//import com.example.securityskilltesting.Repo.ProductRepo;
//import com.example.securityskilltesting.Repo.UserRepo;
//import jakarta.transaction.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.http.HttpStatus;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//
//public class CartService {
//    private final CartRepo cartRepo;
//    private final ProductRepo productRepo;
//    private final CartItemRepo cartItemRepo;
//    private final UserRepo userRepo;
//
//    @Transactional
//    public Cart     addToCart(String username, Long productId, int  quantity){
//
//        UserEntity userEnity= userRepo.findByUsername(username)
//                .orElseThrow(()->new RuntimeException("User not found"));
//        products product= productRepo.findById(productId)
//                .orElseThrow(()->new RuntimeException("product not found"));
////      UserEntity validuser = userRepo.findById(user.getId()).orElseThrow(()->new RuntimeException("User not found"));
//
//
//        Cart cart = cartRepo.findByUserEntity(userEnity)
//                .orElseGet(()->{
//                    Cart newCart= new Cart();
//                    newCart.setUserEntity( userEnity);
//                    return cartRepo.save(newCart);
//                });
//
//        Optional<CartItem>  existingItem=cart.getItems().stream()
//                .filter(item->item.getProduct().getId()==productId)
//                .findFirst();
//        if (existingItem.isPresent()){
//            CartItem cartItem=existingItem.get();
//            cartItem.setQuantity(cartItem.getQuantity()+quantity);
//           cartItemRepo.save(cartItem);
//        }else{
//            CartItem cartItem=new CartItem();
//            cartItem.setCart(cart);
//            cartItem.setProduct(product);
//            cartItem.setQuantity(quantity);
//            cartItem.setUnitPrice(product.getPrice());
//            cartItemRepo.save(cartItem);
//            cart.getItems().add(cartItem);
//
//        }
//
//        return  cartRepo.save(cart);
//
//    }
//
//    @Transactional
//    public Cart removeFromCart(String username,Long productId){
//        UserEntity user=userRepo.findByUsername(username).orElseThrow(
//                ()->new RuntimeException("User not found"));
//        Cart cart =cartRepo.findByUserEntity(user)
//                .orElseThrow(()->new RuntimeException("Cart not found for user"));
//        cart.getItems().removeIf(item->item.getProduct().getId()== productId);
//        return cartRepo.save(cart);
//
//    }
//
//public Cart getCartByUsername(String username){
//    UserEntity user=userRepo.findByUsername(username).orElseThrow(
//            ()->new RuntimeException("User not found"));
//    return cartRepo.findByUserEntity(user)
//            .orElseThrow(()->new RuntimeException("Cart not found for user"));
//}
//    // Inside CartService
//    private void checkUserRole(UserEntity user, String requiredRole) {
//        boolean hasRole = user.getRoles().stream()
//                .anyMatch(role -> role.getName().equals(requiredRole));
//        if (!hasRole) {
//            throw new RuntimeException("User does not have required role: " + requiredRole);
//        }
//    }
//
//}
