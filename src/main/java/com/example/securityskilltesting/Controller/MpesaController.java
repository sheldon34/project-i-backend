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
@RequestMapping("api/payments")
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
            log.error(e.getMessage());
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/stk-push")
    public ResponseEntity<STKPushResponse> stkPush(@RequestBody Map<String,String> payload){
        try{
            return ResponseEntity.ok(mpesaService.initiateSTKPush(payload.get("phoneNumber"),payload.get("amount")));

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
