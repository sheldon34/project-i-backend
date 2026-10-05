package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.Daraja.AccessTokenResponse;
import com.example.securityskilltesting.Dto.Daraja.STKPushResponse;
import com.example.securityskilltesting.Service.MpesaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/payments", "/api/payment/mpesa", "/api/payment", "/api/mpesa", "/mpesa"})
@Slf4j
@RequiredArgsConstructor
public class MpesaController {
    private final MpesaService mpesaService;

    @GetMapping("/access-token")
    public ResponseEntity<AccessTokenResponse> getAccessToken(){
        try{
            return ResponseEntity.ok(mpesaService.generateAccessToken());
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/stk-push")
    public ResponseEntity<STKPushResponse> stkPush(@RequestBody Map<String, Object> payload){
        try{
            Object phoneObj = payload != null ? payload.get("phoneNumber") : null;
            if (phoneObj == null && payload != null) phoneObj = payload.get("phone");
            if (phoneObj == null && payload != null) phoneObj = payload.get("phone_number");
            if (phoneObj == null && payload != null) phoneObj = payload.get("PhoneNumber");

            Object amountObj = payload != null ? payload.get("amount") : null;
            if (amountObj == null && payload != null) amountObj = payload.get("Amount");

            String phoneNumber = phoneObj != null ? String.valueOf(phoneObj).trim() : null;
            String amount = amountObj != null ? String.valueOf(amountObj).trim() : null;

            return ResponseEntity.ok(mpesaService.initiateSTKPush(phoneNumber, amount));
        } catch (Exception e) {
            log.error("STK push error: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
