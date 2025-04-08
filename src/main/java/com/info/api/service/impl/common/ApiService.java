package com.info.api.service.impl.common;

import com.info.api.constants.Constants;
import com.info.api.dto.PaymentApiRequest;
import com.info.api.dto.PaymentApiResponse;
import com.info.api.dto.SearchApiRequest;
import com.info.api.dto.SearchApiResponse;
import com.info.api.dto.ic.APIResponse;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.ic.TransactionReportRequestBody;
import com.info.api.service.ic.ICConfirmTransactionStatusService;
import com.info.api.service.ic.ICPaymentReceiveService;
import com.info.api.service.ic.ICRetrievePaymentStatusService;
import com.info.api.service.ic.ICTransactionReportService;
import com.info.api.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.info.api.util.ObjectConverter.convertObjectToString;
import static com.info.api.util.ParseUtil.*;
import static com.info.api.util.PasswordUtil.generateBase64Hash;

@Service
@RequiredArgsConstructor
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private final ICPaymentReceiveService icPaymentReceiveService;
    private final ICTransactionReportService icTransactionReportService;
    private final ICRetrievePaymentStatusService icRetrievePaymentStatusService;
    private final ICConfirmTransactionStatusService icConfirmTransactionStatusService;

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    @Value("${INSTANT_CASH_API_USER_ID}")
    String icUserId;
    @Value("${INSTANT_CASH_API_USER_PASSWORD}")
    String icPassword;

    public String searchRemittance(SearchApiRequest apiRequest, HttpServletRequest request) {
        logger.info("Enter into ApiService: searchRemittance()");
        SearchApiResponse searchApiResponse = new SearchApiResponse();
        try {
            if (isNullOrEmpty(apiRequest.getBruserid()) || isNullOrEmpty(apiRequest.getBrcode()) || isNullOrEmpty(apiRequest.getExchcode()) || isNullOrEmpty(apiRequest.getPinno())) {
                searchApiResponse.setErrorMessage(buildErrorMessage(apiRequest));
            } else {
                ICExchangePropertyDTO icExchangeProperties = ApiUtil.getICExchangeProperties();
                if (icExchangeProperties.getExchangeCode().equals(apiRequest.getExchcode())) {
                    searchApiResponse = icPaymentReceiveService.paymentReceive(icExchangeProperties, apiRequest);
                } else {
//                    searchApiResponse = riaCashPaymentService.searchRemittance(apiRequest);
                }
            }

        } catch (Exception e) {
            searchApiResponse.setErrorMessage(e.getMessage());
        }

        return convertObjectToString(searchApiResponse);
    }

    public String payRemittance(String data, HttpServletRequest request) {
        logger.info("RequestBody from CBS =================> {} ", data);
        PaymentApiResponse paymentApiResponse = new PaymentApiResponse();

        PaymentApiRequest paymentApiRequest = parseAndPrepareRequest(data, request.getRemoteAddr());
        String header = Objects.nonNull(request.getHeader(X_FORWARDED_FOR)) ? request.getHeader(X_FORWARDED_FOR) : request.getRemoteAddr();
        paymentApiRequest.setIpAddress(header);
        if (!isValidPaymentRequest(paymentApiRequest)) {
            paymentApiResponse = mapPaymentApiResponse(paymentApiResponse, paymentApiRequest);
        } else {
            ICExchangePropertyDTO icExchangeProperties = ApiUtil.getICExchangeProperties();
            if (icExchangeProperties.getExchangeCode().equals(paymentApiRequest.getExchCode())) {
                paymentApiResponse = icConfirmTransactionStatusService.confirmCahTransactionPayment(paymentApiResponse, paymentApiRequest, icExchangeProperties);
            } else {
//                paymentApiResponse = riaCashPaymentService.payRemittance(paymentApiRequest);
            }
        }
        if (logger.isInfoEnabled()) logger.info("Exit from ApiService: payRemittance().");
        return convertObjectToString(paymentApiResponse);
    }

    public APIResponse<Object> fetchTransactionReport(TransactionReportRequestBody report) {
        logger.info("Enter into ApiService: payRemittance()");
        APIResponse<Object> apiResponse = new APIResponse<>();
        logger.info("\nRequestBody from CBS =================> \n{} ", report);
        if (!isValidICTransactionReportBody(report)) return mapInvalidParameters(apiResponse);

        ICExchangePropertyDTO icExchangeProperties = ApiUtil.getICExchangeProperties();
        if (icExchangeProperties.getExchangeCode().equals(report.getExchcode())) {
            icExchangeProperties.setPassword(generateBase64Hash(icUserId, icPassword));
            apiResponse = icTransactionReportService.fetchICTransactionReport(icExchangeProperties, report);
        }

        return apiResponse;
    }

    public APIResponse<Object> getPaymentStatus(String exchcode, String reference) {
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setApiStatus(Constants.API_STATUS_VALID);
        ICExchangePropertyDTO icExchangeProperties = ApiUtil.getICExchangeProperties();
        if (icExchangeProperties.getExchangeCode().equals(exchcode)) {
            icExchangeProperties.setPassword(generateBase64Hash(icUserId, icPassword));
            apiResponse = icRetrievePaymentStatusService.getPaymentStatus(icExchangeProperties, reference);
        }
        return apiResponse;
    }

    public String buildErrorMessage(SearchApiRequest apiRequest) {
        return "Required field should not empty (). bruserid = " + apiRequest.getBruserid() + ",brcode = " + apiRequest.getBrcode() + ",exchcode = " + apiRequest.getExchcode() + ",pinno = " + apiRequest.getPinno();
    }

    private APIResponse mapInvalidParameters(APIResponse apiResponse) {
        logger.error("fetchTransactionReport() errorMessage: {}", Constants.INVALID_PARAMETERS);
        apiResponse.setErrorMessage("Invalid parameters");
        apiResponse.setApiStatus(Constants.API_STATUS_INVALID);
        return apiResponse;
    }

}
