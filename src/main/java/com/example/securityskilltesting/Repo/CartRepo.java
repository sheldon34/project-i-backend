package com.example.securityskilltesting.Repo;

import com.example.securityskilltesting.Entity.Cart;
import com.example.securityskilltesting.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserEntity_Id(Long userId);
    Optional<Cart> findByUserEntity(UserEntity userEntity);


}
