package com.example.securityskilltesting.Repo;

import com.example.securityskilltesting.Entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.relation.Role;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<RolesEntity,Integer> {
    Optional<RolesEntity> findByName(String role);

}
