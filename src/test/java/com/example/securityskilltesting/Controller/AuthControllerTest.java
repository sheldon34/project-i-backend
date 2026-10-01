package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.UsersDto;
import com.example.securityskilltesting.Entity.RolesEntity;
import com.example.securityskilltesting.Entity.UserEntity;
import com.example.securityskilltesting.Repo.RoleRepo;
import com.example.securityskilltesting.Repo.UserRepo;
import com.example.securityskilltesting.security.JWTGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private JWTGenerator jwtGenerator;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerCreatesUserRoleWhenItIsMissing() {
        UsersDto registerDto = new UsersDto();
        registerDto.setUsername("new-user");
        registerDto.setPassword("password");
        RolesEntity userRole = new RolesEntity();
        userRole.setName("USER");

        when(userRepo.existsByUsername("new-user")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(roleRepo.findByName("USER")).thenReturn(Optional.empty());
        when(roleRepo.save(any(RolesEntity.class))).thenReturn(userRole);

        ResponseEntity<String> response = authController.register(registerDto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepo).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRoles()).containsExactly(userRole);
    }
}