package com.example.securityskilltesting.Service;

import com.example.securityskilltesting.Dto.Daraja.AccessTokenResponse;
import com.example.securityskilltesting.Dto.Daraja.STKPushRequest;
import com.example.securityskilltesting.Dto.Daraja.STKPushResponse;
import com.example.securityskilltesting.Utils.Helper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.RemoteException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpesaService {
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    @Value("${daraja.consumer-key}")
    private String consumerKey;
    @Value("${daraja.consumer-secret}")
    private String consumerSecret;
    @Value("${daraja.business-short-code}")
    private String businessShortCode;

    @Value("${daraja.stk-push-callback-url}")
    private String stkPushCallbackUrl;
    @Value("${daraja.stk-push-url}")
    private String stkPushUrl;

    @Value("${daraja.passkey}")
    private String passKey;

    public AccessTokenResponse generateAccessToken() throws IOException {
        String credentials = Credentials.basic(consumerKey, consumerSecret);
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("Authorization", credentials)
                .build();


        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed  to generate access token");

            }
            return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);

        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public STKPushResponse initiateSTKPush(String phoneNumber, String amount) throws IOException {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required ");
        }
        phoneNumber = phoneNumber.trim().replace(" ", "").replace("-", "");
        if (phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.substring(1);
        }
        if (phoneNumber.startsWith("0")) {
            phoneNumber = "254" + phoneNumber.substring(1);
        }

        if (amount == null || amount.isBlank()) {
            amount = "1";
        } else {
            amount = amount.trim();
            if (amount.contains(".")) {
                try {
                    amount = String.valueOf((long) Double.parseDouble(amount));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        AccessTokenResponse accessTokenResponse = generateAccessToken();

        String token = accessTokenResponse.getAccessToken();
        String timestamp = Helper.getTimestamp();
        String password = Helper.toBase64(businessShortCode + passKey + timestamp);
        STKPushRequest stkPushRequest = new STKPushRequest(
                businessShortCode, password, timestamp, "CustomerPayBillOnline", amount,
                phoneNumber, businessShortCode, phoneNumber, stkPushCallbackUrl, "Test", "Test"
        );
        String jsonRequest = objectMapper.writeValueAsString(stkPushRequest);
        RequestBody requestBody = RequestBody.create(jsonRequest, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(stkPushUrl)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + token)
//                .addHeader("Content-Type", "application/json")

                .build();
        log.warn("Access toke: {}", token);

        try (Response response = okHttpClient.newCall(request).execute()) {
            String responseBody = response.body() != null
                    ? response.body().string() : "{}";
            log.info("STK Push  status: {}", response.code());
            log.info("STK Push RAW response: {}", responseBody);
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to initiate STK  push" + responseBody);

            }
            return objectMapper.readValue(responseBody, STKPushResponse.class);
        }
    }

}
