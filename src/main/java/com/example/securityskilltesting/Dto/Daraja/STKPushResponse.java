package com.example.securityskilltesting.Dto.Daraja;

import com.fasterxml.jackson.annotation.JsonProperty;

public class STKPushResponse {
    @JsonProperty("MerchantRequestID")
    private String merchantRequestID;
    @JsonProperty("CheckoutRequestID")
    private  String checkoutRequestID;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("CustomerMessage ")
    private String customerMessage;
}
