package com.ultra.ecommerce.repository;

import com.ultra.ecommerce.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByAuthority(String authority);
}
