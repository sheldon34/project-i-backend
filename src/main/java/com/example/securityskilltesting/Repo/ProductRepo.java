package com.example.securityskilltesting.Repo;

import com.example.securityskilltesting.Entity.products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<products,Long> {
}
