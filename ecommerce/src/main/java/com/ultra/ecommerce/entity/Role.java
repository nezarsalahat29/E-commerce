package com.ultra.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "authority")
    private String authority;
}