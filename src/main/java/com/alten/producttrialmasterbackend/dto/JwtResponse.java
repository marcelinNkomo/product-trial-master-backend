package com.alten.producttrialmasterbackend.dto;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type;

    public JwtResponse(String token) {
        this.token = token;
        this.type = "Bearer";
    }


}
