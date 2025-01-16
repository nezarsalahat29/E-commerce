package com.ultra.ecommerce.enums;

public enum Roles {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    Roles(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
