package com.example.securityskilltesting.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String tokenType="Bearer";
    public AuthResponseDto(String accessToken){
        this.accessToken=accessToken;
    }
}
