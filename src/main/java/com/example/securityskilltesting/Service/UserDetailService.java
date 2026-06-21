package com.example.securityskilltesting.Service;

import com.example.securityskilltesting.Entity.RolesEntity;
import com.example.securityskilltesting.Entity.UserEntity;
import com.example.securityskilltesting.Repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
///The UserDetailService is used to load user-specific data during the authentication process
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity users=userRepo.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User not found"));
        return  new User(users.getUsername(),users.getPassword(),mapRolesToAuthorities(users.getRoles()));
    }
    private Collection<GrantedAuthority>mapRolesToAuthorities(List<RolesEntity>roles){
        return roles.stream().map(role-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
