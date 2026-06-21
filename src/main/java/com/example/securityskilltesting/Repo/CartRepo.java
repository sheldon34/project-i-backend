package com.example.securityskilltesting.Repo;

import com.example.securityskilltesting.Entity.Cart;
import com.example.securityskilltesting.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {
    // Example in CartRepo
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.product WHERE c.id = :cartId")
    Optional<Cart> findByIdWithItemsAndProducts(@Param("cartId") Long cartId);
//    Optional<Cart> findByUserEntity_Id(Long userId);
    Optional<Cart> findByUserEntity(UserEntity userEntity);

    // In CartRepo.java
    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.items i " +
            "LEFT JOIN FETCH i.product " +
            "WHERE c.userEntity = :user")
    Optional<Cart> findByUserEntityWithItemsAndProducts(@Param("user") UserEntity user);
}
