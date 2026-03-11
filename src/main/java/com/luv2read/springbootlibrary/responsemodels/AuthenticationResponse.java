package com.luv2read.springbootlibrary.responsemodels;

public record AuthenticationResponse(
    String token,
    String email,
    String name,
    Boolean isAdmin,
    String role
) {}
