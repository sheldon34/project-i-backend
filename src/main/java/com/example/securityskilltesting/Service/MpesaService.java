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
    String credentials = Credentials.basic(
            consumerKey != null ? consumerKey.trim() : "",
            consumerSecret != null ? consumerSecret.trim() : ""
    );
    Request request = new Request.Builder()
            .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
            .get()
            .addHeader("Authorization", credentials)
            .build();


    try (Response response = okHttpClient.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new IOException("Failed to generate access token");
        }

        return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);
    }
    catch (Exception e) {
        throw new RemoteException(e.getMessage());
    }
}


public STKPushResponse initiateSTKPush(String phoneNumber, String amount) throws IOException {
    if (phoneNumber == null || phoneNumber.isBlank()) {
        throw new IllegalArgumentException("Phone number is required");
    }
    String formattedPhone = phoneNumber.trim().startsWith("0") ? phoneNumber.trim().replaceFirst("0", "254") : phoneNumber.trim();
    AccessTokenResponse accessTokenResponse = generateAccessToken();

    String token = accessTokenResponse.getAccessToken();
    String timestamp = Helper.getTimestamp();
    String code = businessShortCode != null ? businessShortCode.trim() : "";
    String key = passKey != null ? passKey.trim() : "";
    String callbackUrl = stkPushCallbackUrl != null ? stkPushCallbackUrl.trim().replace("\"", "") : "";
    String password = Helper.toBase64(code + key + timestamp);
    STKPushRequest stkPushRequest = new STKPushRequest(
            code, password, timestamp, "CustomerPayBillOnline", amount,
            formattedPhone, code, formattedPhone, callbackUrl, "Test", "Test"
    );
    String jsonRequest = objectMapper.writeValueAsString(stkPushRequest);
    RequestBody requestBody = RequestBody.create(jsonRequest, MediaType.parse("application/json"));


    Request request = new Request.Builder()
            .url(stkPushUrl != null ? stkPushUrl.trim() : "")
            .post(requestBody)
            .addHeader("Authorization", "Bearer " + token)
            .build();



    log.warn("Access token: {}", token);
    try (Response response = okHttpClient.newCall(request).execute()) {
        String responseBody = response.body() != null ? response.body().string() : "{}";
        log.info("STK push status {}", response.code());
        log.info("STK PUSH RAW response: {}", responseBody);


        if (!response.isSuccessful()) {
            throw new RuntimeException("STK push failed with status code: " + response.code());
        }
        return objectMapper.readValue(responseBody, STKPushResponse.class);
    }
}

}
