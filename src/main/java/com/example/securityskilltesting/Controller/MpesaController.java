package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.Daraja.AccessTokenResponse;
import com.example.securityskilltesting.Dto.Daraja.STKPushPaymentRequest;
import com.example.securityskilltesting.Dto.Daraja.STKPushRequest;
import com.example.securityskilltesting.Dto.Daraja.STKPushResponse;
import com.example.securityskilltesting.Service.MpesaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mpesa")
@Slf4j
@RequiredArgsConstructor
public class MpesaController {

    private final MpesaService mpesaService;


    @GetMapping("/access-token")
 public ResponseEntity<AccessTokenResponse>getAccessToken(){
        try{
            return ResponseEntity.ok(mpesaService.generateAccessToken());
        }
        catch (Exception e){
            log.error("Error generating access token: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/stkpush")
    public ResponseEntity<STKPushResponse> stkPush(
            @RequestBody(required = false) STKPushPaymentRequest paymentRequest,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "amount", required = false) String amount) {
        try {
            String targetPhone = (paymentRequest != null && paymentRequest.getPhoneNumber() != null)
                    ? paymentRequest.getPhoneNumber() : phoneNumber;
            String targetAmount = (paymentRequest != null && paymentRequest.getAmount() != null)
                    ? paymentRequest.getAmount() : amount;

            if (targetPhone == null || targetAmount == null) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(mpesaService.initiateSTKPush(targetPhone, targetAmount));
        } catch (Exception e) {
            log.error("Error initiating STK push: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<String> mpesaCallback(@RequestBody(required = false) Map<String, Object> callbackPayload) {
        log.info("Received M-Pesa STK Push Callback: {}", callbackPayload);
        return ResponseEntity.ok("Callback received successfully");
    }
}

