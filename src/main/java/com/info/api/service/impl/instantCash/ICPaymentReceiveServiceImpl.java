package com.info.api.service.impl.instantCash;

import com.info.api.entity.ApiTrace;
import com.info.api.entity.ICCashRemittanceData;
import com.info.api.mapper.ICPaymentReceiveRemittanceMapper;
import com.info.api.dto.instantCash.ICExchangePropertyDTO;
import com.info.api.dto.instantCash.ICOutstandingTransactionDTO;
import com.info.api.dto.SearchApiResponse;
import com.info.api.service.common.ApiTraceService;
import com.info.api.service.common.CommonService;
import com.info.api.service.instantCash.ICCashRemittanceDataService;
import com.info.api.service.instantCash.ICPaymentReceiveService;
import com.info.api.util.ApiUtil;
import com.info.api.util.Constants;
import com.info.api.util.ObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.info.api.util.ObjectConverter.convertObjectToString;

@Service
@Transactional
public class ICPaymentReceiveServiceImpl implements ICPaymentReceiveService {

    public static final Logger logger = LoggerFactory.getLogger(ICPaymentReceiveServiceImpl.class);

    @Value("${IC_API_FINANCIAL_ID:BD01RH}")
    public String IC_API_FINANCIAL_ID;

    private final RestTemplate restTemplate;
    private final CommonService commonService;

    private final ApiTraceService apiTraceService;

    private final ICPaymentReceiveRemittanceMapper mapper;
    private final ICCashRemittanceDataService icCashRemittanceDataService;


    @Value("${bank.code}")
    private String bankCode;

    public ICPaymentReceiveServiceImpl(RestTemplate restTemplate, CommonService commonService, ApiTraceService apiTraceService, ICPaymentReceiveRemittanceMapper mapper, ICCashRemittanceDataService icCashRemittanceDataService) {
        this.restTemplate = restTemplate;
        this.commonService = commonService;
        this.apiTraceService = apiTraceService;
        this.mapper = mapper;
        this.icCashRemittanceDataService = icCashRemittanceDataService;
    }


    @Override
    public SearchApiResponse paymentReceive(@NotNull ICExchangePropertyDTO dto, @NotNull String referenceNo) {
        SearchApiResponse searchApiResponse = new SearchApiResponse();
        searchApiResponse.setPinno(referenceNo);
        searchApiResponse.setReference(referenceNo);
        searchApiResponse.setExchCode(dto.getExchangeCode());
        searchApiResponse.setOriginalRequest(referenceNo);

        if (icCashRemittanceDataService.findByExchangeCodeAndReferenceNo(dto.getExchangeCode(), referenceNo).isPresent()) {
            searchApiResponse.setErrorMessage(Constants.REFERENCE_NO_ALREADY_EXIST);
            return searchApiResponse;
        }

        ApiUtil.validateICExchangePropertiesBeforeProceed(dto, dto.getPaymentReceiveUrl(), Constants.EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_RECEIVE_PAYMENT);

        String response = "";
        String uuid = UUID.randomUUID().toString();
        ICCashRemittanceData icCashRemittanceData = new ICCashRemittanceData();
        ApiTrace apiTrace = apiTraceService.create(dto.getExchangeCode(), Constants.REQUEST_TYPE_DOWNLOAD_REQ, null);
        Optional.ofNullable(apiTrace).orElseThrow(() -> new RuntimeException("Unable to create ApiTrace!"));

        try {
            String paymentUrl = dto.getPaymentReceiveUrl().trim() + "?reference=" + referenceNo.trim();
            System.out.println("paymentUrl: " + paymentUrl);
            HttpEntity<String> httpEntity = ApiUtil.createHttpEntity("", uuid, dto);
            ResponseEntity<ICOutstandingTransactionDTO> responseEntity = restTemplate.exchange(paymentUrl, HttpMethod.GET, httpEntity, ICOutstandingTransactionDTO.class);
            searchApiResponse.setOriginalResponse(String.valueOf(responseEntity.getBody()));

            if ((responseEntity.getStatusCode().equals(HttpStatus.OK)) && Objects.nonNull(responseEntity.getBody())) {
                response = convertObjectToString(responseEntity.getBody());
                icCashRemittanceData = mapper.prepareICCashRemittanceData(icCashRemittanceData, responseEntity.getBody(), dto.getExchangeCode(), apiTrace, true);

                if (Objects.nonNull(icCashRemittanceData)) {
                    icCashRemittanceData = icCashRemittanceDataService.save(icCashRemittanceData);
                }
                mapper.mapSearchApiResponse(searchApiResponse, icCashRemittanceData);
                apiTrace.setStatus(Constants.API_STATUS_VALID);
            } else {
                searchApiResponse.setApiStatus(Constants.API_STATUS_VALID);
                searchApiResponse.setErrorMessage(ObjectConverter.convertObjectToString(responseEntity.getBody()));
                apiTraceService.deleteById(apiTrace.getId());
                logger.info("Tracing removed because no record found, uuid: " + uuid);
                return searchApiResponse;
            }
            searchApiResponse.setPayoutStatus(String.valueOf(responseEntity.getStatusCode().value()));
            searchApiResponse.setApiStatus(String.valueOf(responseEntity.getStatusCode().value()));
            logger.info("Execute paymentReceive for ReferenceNo: {}", referenceNo);
        } catch (Exception e) {
            response = e.getMessage();
            apiTrace.setStatus(Constants.API_STATUS_ERROR);
            searchApiResponse.setApiStatus(Constants.API_STATUS_INVALID);
            searchApiResponse.setErrorMessage(ObjectConverter.convertObjectToString(response));
            logger.error("Error in paymentReceive for ReferenceNo: " + referenceNo, uuid, e);
        }

        apiTrace.setRequestMsg(referenceNo);
        apiTrace.setResponseMsg(response);
        apiTrace.setCorrelationId(uuid);
        apiTraceService.save(apiTrace);
        logger.info("paymentReceive successful for ReferenceNo: " + referenceNo, uuid);
        return searchApiResponse;
    }


}
