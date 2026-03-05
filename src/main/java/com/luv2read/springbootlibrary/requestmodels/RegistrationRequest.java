package com.luv2read.springbootlibrary.requestmodels;

import lombok.Data;

@Data
public class RegistrationRequest {
    
    private String email;
    private String password;
    private String name;
}
