package com.info.api.controller;

import com.info.api.dto.ic.APIResponse;
import com.info.api.service.impl.common.ApiService;
import com.info.api.service.ic.ICUnlockRemittanceService;
import com.info.api.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping("/apiservice")
@RequiredArgsConstructor
public class InstantCashController {

    private final ApiService apiService;
    private final ICUnlockRemittanceService icUnlockRemittanceService;

    @GetMapping(value = "/paymentStatus")
    public ResponseEntity<APIResponse> getPaymentStatus(@RequestHeader @NotNull String userId, @RequestHeader @NotNull String password, @RequestParam("exchcode") String exchcode, @RequestParam @NotNull String reference) {
        return ResponseEntity.ok().body(apiService.getPaymentStatus(exchcode, reference));
    }

    @PostMapping(value = "/unlock")
    public ResponseEntity<APIResponse> unlockRemittance(@RequestHeader String userId, @RequestHeader String password, @RequestParam String reference) {
        return ResponseEntity.ok().body(icUnlockRemittanceService.unlockICOutstandingRemittance(reference, ApiUtil.getICExchangeProperties()));
    }

}
