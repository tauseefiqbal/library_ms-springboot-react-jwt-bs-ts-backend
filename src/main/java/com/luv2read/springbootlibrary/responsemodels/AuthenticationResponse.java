package com.luv2read.springbootlibrary.responsemodels;

public class AuthenticationResponse {
    
    private String token;
    private String email;
    private String name;
    private Boolean isAdmin;
    private String role;

    public AuthenticationResponse() {}

    public AuthenticationResponse(String token, String email, String name, Boolean isAdmin, String role) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
