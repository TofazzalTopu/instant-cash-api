package com.info.api.service.impl.ic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.api.constants.Constants;
import com.info.api.dto.PaymentApiResponse;
import com.info.api.dto.SearchApiRequest;
import com.info.api.dto.SearchApiResponse;
import com.info.api.entity.*;
import com.info.api.mapper.ICPaymentReceiveRemittanceMapper;
import com.info.api.dto.ic.ICConfirmDTO;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.ic.ICOutstandingTransactionDTO;
import com.info.api.service.common.ApiTraceService;
import com.info.api.service.ic.ICCashRemittanceDataService;
import com.info.api.service.ic.ICConfirmTransactionStatusService;
import com.info.api.service.ic.ICPaymentReceiveService;
import com.info.api.util.ApiUtil;
import com.info.api.util.ObjectConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.info.api.util.ObjectConverter.convertObjectToString;
import static com.info.api.util.PasswordUtil.generateBase64Hash;

@Service
@Transactional
@RequiredArgsConstructor
public class ICPaymentReceiveServiceImpl implements ICPaymentReceiveService {
    public static final Logger logger = LoggerFactory.getLogger(ICPaymentReceiveServiceImpl.class);

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ApiTraceService apiTraceService;
    private final ICPaymentReceiveRemittanceMapper mapper;
    private final ICCashRemittanceDataService icCashRemittanceDataService;
    private final ICConfirmTransactionStatusService icConfirmTransactionStatusService;

    @Value("${INSTANT_CASH_API_USER_PASSWORD}")
    String icPassword;

    @Value("${INSTANT_CASH_API_USER_RECEIVE_PAYMENT_API_USER_ID}")
    String icPaymentUserId;

    @Override
    public SearchApiResponse paymentReceive(@NotNull ICExchangePropertyDTO dto, SearchApiRequest searchApiRequest) {
        SearchApiResponse searchApiResponse = mapSearchApiResponse(searchApiRequest);
        searchApiResponse.setExchCode(dto.getExchangeCode());
        dto.setIcPaymentUserId(icPaymentUserId);
        dto.setPassword(generateBase64Hash(icPaymentUserId, icPassword));

        if (ApiUtil.validateIsICPropertiesIsNotExist(dto, dto.getPaymentReceiveUrl())) {
            return mapper.createErrorResponse(searchApiResponse, Constants.EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_RECEIVE_PAYMENT);
        }

        if (icCashRemittanceDataService.findByExchangeCodeAndReferenceNo(dto.getExchangeCode(), searchApiRequest.getPinno()).isPresent()) {
            searchApiResponse.setErrorMessage(Constants.REFERENCE_NO_ALREADY_EXIST);
            return searchApiResponse;
        }

        String status = "";
        ApiTrace apiTrace = apiTraceService.create(dto.getExchangeCode(), Constants.REQUEST_TYPE_SEARCH, null);
        if (Objects.isNull(apiTrace)) return searchApiResponse;
        try {
            String paymentUrl = dto.getPaymentReceiveUrl().trim() + "?reference=" + searchApiRequest.getPinno().trim();
            logger.info("Execute paymentReceive for ReferenceNo {} and Payment URL: {}", searchApiRequest.getPinno(), paymentUrl);
            ResponseEntity<ICOutstandingTransactionDTO> responseEntity = restTemplate.exchange(paymentUrl, HttpMethod.GET, ApiUtil.createHttpEntity("", apiTrace.getId(), dto), ICOutstandingTransactionDTO.class);
            logger.info("PaymentReceive Response: {}", responseEntity);
            ICOutstandingTransactionDTO outstandingDTO = responseEntity.getBody();
            searchApiResponse.setOriginalResponse(convertObjectToString(outstandingDTO));

            if ((responseEntity.getStatusCode().equals(HttpStatus.OK)) && responseEntity.hasBody()) {

                RemittanceData remittanceData = mapper.prepareICCashRemittanceData(responseEntity.getBody(), dto.getExchangeCode(), apiTrace);
                ICCashRemittanceData icCashRemittanceData = objectMapper.convertValue(remittanceData, ICCashRemittanceData.class);
                logger.info("PaymentReceive ICCashRemittanceData: {} \n ", icCashRemittanceData);
                if (Objects.nonNull(icCashRemittanceData)) icCashRemittanceDataService.save(icCashRemittanceData);

                searchApiResponse.setPayoutStatus(String.valueOf(responseEntity.getStatusCode().value()));
                mapper.mapSearchApiResponse(searchApiResponse, icCashRemittanceData);
                status = Constants.API_STATUS_VALID;

//               send IC confirm transaction notification
                ICConfirmDTO icConfirmDTO = ICConfirmDTO.builder().newStatus(ApiUtil.getICTransactionStatus(remittanceData.getProcessStatus())).remarks("Downloaded.").reference(remittanceData.getReferenceNo()).build();
                icConfirmTransactionStatusService.notifyPaymentStatus(new PaymentApiResponse(), dto, remittanceData, icConfirmDTO, searchApiRequest.getPinno(), false);
            } else {
                return mapResponse(searchApiResponse, responseEntity, apiTrace.getId());
            }
        } catch (Exception e) {
            status = Constants.API_STATUS_ERROR;
            searchApiResponse.setOriginalResponse(e.getMessage());
            searchApiResponse.setErrorMessage(e.getMessage());
            logger.error("Error in paymentReceive for ReferenceNo: {} ", searchApiRequest.getPinno(), e);
        }
        searchApiResponse.setApiStatus(status);
        apiTraceService.addToApiTrace(apiTrace.getId(), searchApiResponse, searchApiRequest);
        logger.info("\npaymentReceive for ReferenceNo: {} and Response: {} ", searchApiRequest.getPinno(), searchApiResponse.getOriginalResponse());
        return searchApiResponse;
    }

    private SearchApiResponse mapSearchApiResponse(SearchApiRequest searchApiRequest) {
        SearchApiResponse searchApiResponse = new SearchApiResponse();
        searchApiResponse.setPinno(searchApiRequest.getPinno());
        searchApiResponse.setReference(searchApiRequest.getPinno());
        searchApiResponse.setOriginalRequest(searchApiRequest.getPinno());
        return searchApiResponse;
    }

    private SearchApiResponse mapResponse(SearchApiResponse searchApiResponse, ResponseEntity<?> responseEntity, Long apiTraceId) {
        searchApiResponse.setApiStatus(Constants.API_STATUS_INVALID);
        searchApiResponse.setErrorMessage(ObjectConverter.convertObjectToString(responseEntity.getBody()));
        logger.info(Constants.REFERENCE_NOT_EXIST, "paymentReceive()", apiTraceId);
        return searchApiResponse;
    }

}
