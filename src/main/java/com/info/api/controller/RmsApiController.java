package com.info.api.controller;

import com.info.api.dto.SearchApiRequest;
import com.info.api.dto.ic.APIResponse;
import com.info.api.dto.ic.TransactionReportRequestBody;
import com.info.api.service.impl.common.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/apiservice"})
public class RmsApiController {

    private final ApiService apiService;


    @GetMapping(value = "/remittance")
    public ResponseEntity<String> searchRemittance(@RequestHeader @NotNull String userId, @RequestHeader String password, @RequestParam String bruserid, @RequestParam String brcode, @RequestParam String exchcode, @RequestParam String pinno, HttpServletRequest request) {
        SearchApiRequest searchApiRequest = new SearchApiRequest(bruserid, brcode, exchcode, pinno, null);
        return new ResponseEntity<>(apiService.searchRemittance(searchApiRequest, request), HttpStatus.OK);
    }

    @PutMapping(value = "/remittance")
    public ResponseEntity<String> payRemittance(@RequestHeader String userId, @RequestHeader String password, @RequestBody String data, HttpServletRequest request) {
        return new ResponseEntity<>(apiService.payRemittance(data, request), HttpStatus.OK);
    }

    @GetMapping(value = "/transaction-report")
    public ResponseEntity<APIResponse> transactionReport(@RequestHeader @NotNull String userId, @RequestHeader @NotNull String password,
                                                         @RequestParam @NotNull String exchcode, @RequestParam @NotNull String fromDate,
                                                         @RequestParam @NotNull String toDate, @RequestParam int pageNumber, @RequestParam int pageSize, HttpServletRequest request) {
        TransactionReportRequestBody report = new TransactionReportRequestBody(userId, password, null, null, exchcode, fromDate, toDate, pageNumber, pageSize);
        return new ResponseEntity<>(apiService.fetchTransactionReport(report), HttpStatus.OK);
    }



}

