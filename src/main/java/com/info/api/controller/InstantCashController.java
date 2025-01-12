package com.info.api.controller;

import com.info.api.dto.ApiResponse;
import com.info.api.service.common.CommonService;
import com.info.api.service.ic.ICPaymentReceiveService;
import com.info.api.service.ic.ICRetrievePaymentStatusService;
import com.info.api.service.ic.ICUnlockRemittanceService;
import com.info.api.util.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static com.info.api.util.Constants.*;

@RestController
@RequestMapping("/instantCash")
@CrossOrigin(origins = "localhost:3000", allowCredentials = "true")
public class InstantCashController {

    private final CommonService commonService;
    private final ICPaymentReceiveService icPaymentReceiveService;
    private final ICUnlockRemittanceService icUnlockRemittanceService;
    private final ICRetrievePaymentStatusService icRetrievePaymentStatusService;


    public InstantCashController(CommonService commonService, ICPaymentReceiveService icPaymentReceiveService, ICUnlockRemittanceService icUnlockRemittanceService, ICRetrievePaymentStatusService icRetrievePaymentStatusService) {
        this.commonService = commonService;
        this.icPaymentReceiveService = icPaymentReceiveService;
        this.icUnlockRemittanceService = icUnlockRemittanceService;
        this.icRetrievePaymentStatusService = icRetrievePaymentStatusService;
    }

    @GetMapping(value = "/receive-payment")
    public ResponseEntity<ApiResponse> receivePayment(@RequestHeader @NotNull String userId, @RequestHeader @NotNull String password, @RequestParam @NotNull String reference) {
        commonService.verifyAuthorization(userId, password);
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), PAYMENT_RECEIVE_SUCCESSFULLY, icPaymentReceiveService.paymentReceive(ApiUtil.getICExchangeProperties(), reference)));
    }

    @GetMapping(value = "/status")
    public ResponseEntity<ApiResponse> retrievePaymentStatus(@RequestHeader @NotNull String userId, @RequestHeader @NotNull String password, @RequestParam @NotNull String reference) {
        commonService.verifyAuthorization(userId, password);
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), STATUS_RETRIEVE_SUCCESSFULLY, icRetrievePaymentStatusService.retrievePaymentStatus(reference, ApiUtil.getICExchangeProperties())));
    }

    @PostMapping(value = "/unlock")
    public ResponseEntity<ApiResponse> unlockRemittance(@RequestHeader @NotNull String userId, @RequestHeader @NotNull String password, @RequestBody @NotNull String reference) {
        commonService.verifyAuthorization(userId, password);
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), PAYMENT_UNLOCK_SUCCESSFULLY, icUnlockRemittanceService.unlockICOutstandingRemittance(reference, ApiUtil.getICExchangeProperties())));
    }

}


