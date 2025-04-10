package com.info.api.controller;

import com.info.api.dto.ic.APIResponse;
import com.info.api.service.impl.common.ApiService;
import com.info.api.service.ic.ICUnlockRemittanceService;
import com.info.api.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/apiservice")
@Tag(name = "Instant Cash", description = "APIs for handling Instant Cash remittance operations")
public class InstantCashController {

    private final ApiService apiService;
    private final ICUnlockRemittanceService icUnlockRemittanceService;

    @Operation(description = "Check payment status by exchange code and reference PIN.")
    @GetMapping(value = "/paymentStatus")
    public ResponseEntity<APIResponse<String>> getPaymentStatus(@RequestHeader @NotNull String userId, @RequestHeader @NotNull String password,
                                                                @RequestParam String exchcode, @RequestParam @NotNull String reference) {
        return ResponseEntity.ok().body(apiService.getPaymentStatus(exchcode, reference));
    }

    @Operation(description = "Unlock remittance by reference PIN.")
    @PostMapping(value = "/unlock")
    public ResponseEntity<APIResponse<String>> unlockRemittance(@RequestHeader String userId, @RequestHeader String password, @RequestParam String reference) {
        return ResponseEntity.ok().body(icUnlockRemittanceService.unlockICOutstandingRemittance(reference, ApiUtil.getICExchangeProperties()));
    }

    @GetMapping(value = "/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok().body("Welcome");
    }

}

