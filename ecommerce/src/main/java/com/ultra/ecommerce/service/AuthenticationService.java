package com.ultra.ecommerce.service;

import com.ultra.ecommerce.dtos.RegisterUserDto;
import com.ultra.ecommerce.entity.Role;
import com.ultra.ecommerce.entity.User;
import com.ultra.ecommerce.repository.RoleRepository;
import com.ultra.ecommerce.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ultra.ecommerce.dtos.LoginUserDto;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User signup(RegisterUserDto input) {
        String role = input.isUserAdmin() ? "ROLE_ADMIN" : "ROLE_USER";
        Role userRole = roleRepository.findByAuthority(role).orElseThrow(() -> new NoSuchElementException("Authority not present"));
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setAuthorities(authorities);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
