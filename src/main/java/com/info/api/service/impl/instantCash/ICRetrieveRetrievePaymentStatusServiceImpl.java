package com.info.api.service.impl.instantCash;

import com.info.api.entity.RemittanceData;
import com.info.api.dto.instantCash.ICExchangePropertyDTO;
import com.info.api.dto.instantCash.ICPaymentStatusDTO;
import com.info.api.service.common.ApiTraceService;
import com.info.api.service.common.RemittanceDataService;
import com.info.api.service.instantCash.ICRetrievePaymentStatusService;
import com.info.api.util.ApiUtil;
import com.info.api.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.info.api.util.ObjectConverter.convertObjectToString;

@Service
public class ICRetrieveRetrievePaymentStatusServiceImpl implements ICRetrievePaymentStatusService {

    public static final Logger logger = LoggerFactory.getLogger(ICRetrieveRetrievePaymentStatusServiceImpl.class);

    public static String API_FINANCIAL_ID = "AE01BH";

    private final RestTemplate restTemplate;
    private final ApiTraceService apiTraceService;

    private final RemittanceDataService remittanceDataService;

    public ICRetrieveRetrievePaymentStatusServiceImpl(RestTemplate restTemplate, ApiTraceService apiTraceService, RemittanceDataService remittanceDataService) {
        this.restTemplate = restTemplate;
        this.apiTraceService = apiTraceService;
        this.remittanceDataService = remittanceDataService;
    }

    @Override
    public ICPaymentStatusDTO retrievePaymentStatus(String referenceNo, ICExchangePropertyDTO dto) {
        ApiUtil.validateICExchangePropertiesBeforeProceed(dto, dto.getStatusUrl(), Constants.EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_RETRIEVE_PAYMENT_STATUS);
        ICPaymentStatusDTO icPaymentStatusDTO = new ICPaymentStatusDTO();
        if (ApiUtil.validateIfICPropertiesIsNotExist(dto, dto.getStatusUrl())) {
            logger.error(Constants.EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_RETRIEVE_PAYMENT_STATUS);
            return icPaymentStatusDTO;
        }
        String status = "";
        String response = "";
        String uuid = UUID.randomUUID().toString();
        try {
            String retrievePaymentStatusUrl = dto.getStatusUrl() + "?reference=" + referenceNo;
            ResponseEntity<ICPaymentStatusDTO> responseEntity = restTemplate.exchange(retrievePaymentStatusUrl, HttpMethod.POST,
                    ApiUtil.createHttpEntity("", uuid, dto), ICPaymentStatusDTO.class);

            if ((responseEntity.getStatusCode().equals(HttpStatus.OK)) && Objects.nonNull(responseEntity.getBody())) {
                icPaymentStatusDTO = responseEntity.getBody();
                status = Constants.API_STATUS_VALID;
                response = convertObjectToString(responseEntity.getBody());
            }
            logger.info("Execute retrievePaymentStatus() for ReferenceNo: {}", referenceNo);
        } catch (Exception e) {
            response = e.getMessage();
            status = Constants.API_STATUS_ERROR;
            logger.error("Error in retrievePaymentStatus() for ReferenceNo: " + referenceNo, e);
//            apiTraceService.saveApiTrace(dto.getExchangeCode(), referenceNo, response, status, Constants.REQUEST_TYPE_IC_RETRIEVE_PAYMENT_STATUS_TRANSACTION, uuid);
            throw new RuntimeException(e.getMessage());
        }
//        apiTraceService.saveApiTrace(dto.getExchangeCode(), referenceNo, response, status, Constants.REQUEST_TYPE_IC_RETRIEVE_PAYMENT_STATUS_TRANSACTION, uuid);
        logger.info("retrievePaymentStatus successful for ReferenceNo: " + referenceNo, uuid);

        return icPaymentStatusDTO;
    }


    private void processPaymentStatus(ResponseEntity<ICPaymentStatusDTO> responseEntity, RemittanceData remittanceData) {
        String responseData = "";
        try {
            if (HttpStatus.OK.equals(responseEntity.getStatusCode()) && Objects.nonNull(responseEntity.getBody())) {
                logger.info("RetrievePaymentStatus Response data:" + responseEntity.getBody());
                remittanceData.setApiResponse(convertObjectToString(responseEntity.getBody()));
            } else {
                logger.error("API Responded with different status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error in processPaymentReceive(). Error = " + e);
            logger.info("API request data is -----------------------------> \r\n" + responseData);
        }
    }

}
