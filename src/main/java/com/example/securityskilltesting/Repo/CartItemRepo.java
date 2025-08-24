package com.example.securityskilltesting.Repo;

import com.example.securityskilltesting.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductId (Long  cartId, Long productId );
}
