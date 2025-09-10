package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.AuthResponseDto;
import com.example.securityskilltesting.Dto.UsersDto;
import com.example.securityskilltesting.Entity.RolesEntity;
import com.example.securityskilltesting.Entity.UserEntity;
import com.example.securityskilltesting.Repo.RoleRepo;
import com.example.securityskilltesting.Repo.UserRepo;
import com.example.securityskilltesting.security.JWTGenerator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private JWTGenerator jwtGenerator;


    ///register controller
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UsersDto registerDto){

if (userRepo.existsByUsername(registerDto.getUsername())){
    //confirming if user existi in db
    return new ResponseEntity<>("User taken", HttpStatus.BAD_REQUEST);
}
//making new user
        UserEntity userEntity=new UserEntity();
    userEntity.setUsername(registerDto.getUsername());
    //encoding the user
userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        RolesEntity rolesEntity=roleRepo.findByName("USER").get();
        userEntity.setRoles(Collections.singletonList(rolesEntity));

        userRepo.save(userEntity);
        return new ResponseEntity<>("User registered successfully",HttpStatus.OK);

    }
    /// login dto
    @PostMapping("/login")
    public  ResponseEntity<AuthResponseDto> login(@RequestBody UsersDto loginDto){
       /// authenticating user using password and username

        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                        loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
       String token=jwtGenerator.generateToken(authentication);

     return new ResponseEntity<>(new AuthResponseDto(token),HttpStatus.OK);

    }
//    @PutMapping("/make-admin/{username}")
//    public ResponseEntity<String> makeAdmin(@PathVariable String username) {
//        UserEntity user = userRepo.findByUsername(username).orElseThrow();
//        RolesEntity adminRole = roleRepo.findByName("ADMIN").get();
//        user.setRoles(Collections.singletonList(adminRole));
//        userRepo.save(user);
//        return ResponseEntity.ok("User promoted to admin");
//    }
}
