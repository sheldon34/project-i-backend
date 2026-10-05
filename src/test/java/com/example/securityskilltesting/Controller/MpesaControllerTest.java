package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.Daraja.AccessTokenResponse;
import com.example.securityskilltesting.Dto.Daraja.STKPushResponse;
import com.example.securityskilltesting.Service.MpesaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MpesaControllerTest {

    @Mock
    private MpesaService mpesaService;

    @InjectMocks
    private MpesaController mpesaController;

    @Test
    void getAccessTokenReturnsOkWhenServiceSucceeds() throws IOException {
        AccessTokenResponse tokenResponse = new AccessTokenResponse("fake-token", "3599");
        when(mpesaService.generateAccessToken()).thenReturn(tokenResponse);

        ResponseEntity<AccessTokenResponse> response = mpesaController.getAccessToken();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(tokenResponse);
    }

    @Test
    void getAccessTokenReturns500WhenServiceThrows() throws IOException {
        when(mpesaService.generateAccessToken()).thenThrow(new RuntimeException("Token error"));

        ResponseEntity<AccessTokenResponse> response = mpesaController.getAccessToken();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void stkPushExtractsPhoneNumberAndAmountCorrectly() throws IOException {
        STKPushResponse mockResponse = new STKPushResponse();
        when(mpesaService.initiateSTKPush(eq("0712345678"), eq("100"))).thenReturn(mockResponse);

        Map<String, Object> payload = new HashMap<>();
        payload.put("phoneNumber", "0712345678");
        payload.put("amount", 100);

        ResponseEntity<STKPushResponse> response = mpesaController.stkPush(payload);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockResponse);
        verify(mpesaService).initiateSTKPush("0712345678", "100");
    }

    @Test
    void stkPushHandlesAlternativePayloadKeys() throws IOException {
        STKPushResponse mockResponse = new STKPushResponse();
        when(mpesaService.initiateSTKPush(eq("254712345678"), eq("250"))).thenReturn(mockResponse);

        Map<String, Object> payload = new HashMap<>();
        payload.put("phone", "254712345678");
        payload.put("Amount", "250");

        ResponseEntity<STKPushResponse> response = mpesaController.stkPush(payload);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockResponse);
        verify(mpesaService).initiateSTKPush("254712345678", "250");
    }
}
