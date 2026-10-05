package com.example.securityskilltesting.Dto.Daraja;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class STKPushPaymentRequest {
    private String phoneNumber;
    private String amount;
}
