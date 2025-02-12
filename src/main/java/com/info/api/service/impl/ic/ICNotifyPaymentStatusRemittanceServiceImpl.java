package com.info.api.service.impl.ic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.api.constants.RemittanceDataStatus;
import com.info.api.entity.ICCashRemittanceData;
import com.info.api.entity.RemittanceData;
import com.info.api.dto.ic.ICConfirmDTO;
import com.info.api.dto.ic.ICConfirmResponseDTO;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.PaymentApiResponse;
import com.info.api.service.common.ApiTraceService;
import com.info.api.service.common.RemittanceDataService;
import com.info.api.service.common.RemittanceProcessService;
import com.info.api.service.ic.ICCashRemittanceDataService;
import com.info.api.service.ic.ICNotifyPaymentStatusRemittanceService;
import com.info.api.util.ApiUtil;
import com.info.api.util.Constants;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.info.api.util.ObjectConverter.convertObjectToString;

@Service
@Transactional
public class ICNotifyPaymentStatusRemittanceServiceImpl implements ICNotifyPaymentStatusRemittanceService {

    public static final Logger logger = LoggerFactory.getLogger(ICNotifyPaymentStatusRemittanceServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ApiTraceService apiTraceService;

    private final RemittanceDataService remittanceDataService;

    private final ICCashRemittanceDataService icCashRemittanceDataService;

    private RemittanceProcessService remittanceProcessService;

    public static final String NEW_STATUS_D = "D";
    public static final String NEW_STATUS_X = "X";
    public static final String NEW_STATUS_Y = "Y";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_REJECTED = "REJECTED";

    @Value("${INSTANT_CASH_API_USER_ID}")
    private String INSTANT_CASH_API_USER_ID;

    public ICNotifyPaymentStatusRemittanceServiceImpl(RestTemplate restTemplate, ApiTraceService apiTraceService, RemittanceDataService remittanceDataService, ICCashRemittanceDataService icCashRemittanceDataService, RemittanceProcessService remittanceProcessService) {
        this.restTemplate = restTemplate;
        this.apiTraceService = apiTraceService;
        this.remittanceDataService = remittanceDataService;
        this.icCashRemittanceDataService = icCashRemittanceDataService;
        this.remittanceProcessService = remittanceProcessService;
    }

    @Override
    public List<RemittanceData> notifyPaymentStatus(ICExchangePropertyDTO icDTO) {
        if (ApiUtil.validateIfICPropertiesIsNotExist(icDTO, icDTO.getNotifyRemStatusUrl())) {
            logger.error(Constants.EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_NOTIFY_STATUS);
            return new ArrayList<>();
        }

        String uuid = UUID.randomUUID().toString();
        StringBuilder requestBuilder = new StringBuilder();
        StringBuilder responseBuilder = new StringBuilder();
        StringBuilder statusBuilder = new StringBuilder();

        List<RemittanceData> notifiedRemittances = new ArrayList<>();
        List<RemittanceData> remittanceDataList = findAllToNotifyPaidOrRejectedByExchangeCode(icDTO.getExchangeCode()).stream().distinct().collect(Collectors.toList());

        try {
            remittanceDataList.forEach(remittanceData -> {
                try {
                    String newStatus = getStatus(remittanceData.getProcessStatus());
                    ICConfirmDTO icConfirmDTO = ICConfirmDTO.builder().reference(remittanceData.getReferenceNo()).newStatus(newStatus).remarks("Success").build();
                    requestBuilder.append("{").append(icConfirmDTO).append("},\n");

                    HttpEntity<ICConfirmDTO> httpEntity = ApiUtil.createHttpEntity(icConfirmDTO, uuid, icDTO);
                    ResponseEntity<ICConfirmResponseDTO> responseEntity = restTemplate.exchange(icDTO.getNotifyRemStatusUrl(), HttpMethod.POST, httpEntity, ICConfirmResponseDTO.class);
                    responseBuilder.append("[").append(responseEntity.getBody()).append("}\n");

                    if (Objects.nonNull(newStatus) && (newStatus.equals(NEW_STATUS_X) || newStatus.equals(NEW_STATUS_Y))) {
                        updateNotifiedICRemittanceData(responseEntity, remittanceData);
                        notifiedRemittances.add(remittanceData);
                    }
                    statusBuilder.append("{").append(remittanceData.getReferenceNo()).append("-").append(Constants.API_STATUS_VALID).append("},\n");
                    logger.info("Execute notifyPaymentStatus for ReferenceNo: {}", remittanceData.getReferenceNo());
                } catch (Exception e) {
                    statusBuilder.append("{").append(remittanceData.getReferenceNo()).append("-").append(Constants.API_STATUS_ERROR).append("},\n");
                    responseBuilder.append("{").append(e.getMessage()).append("},\n");
                    logger.error("Error in confirmICOutstandingRemittance for ReferenceNo: {},  ERROR: {}", remittanceData.getReferenceNo(), e.getMessage());
                }
            });

            if (!notifiedRemittances.isEmpty()) {
                remittanceDataList = remittanceDataService.saveAll(notifiedRemittances);
            }
        } catch (Exception e) {
            logger.error("Error in confirmICOutstandingRemittance: ", e);
        }
//        apiTraceService.saveApiTrace(icDTO.getExchangeCode(), requestBuilder.toString(), responseBuilder.toString(), statusBuilder.toString(), Constants.REQUEST_TYPE_NOTIFY_REM_STATUS, uuid);
        return remittanceDataList;
    }

    @Override
    public PaymentApiResponse notifyCashPaymentStatus(PaymentApiResponse paymentApiResponse, ICExchangePropertyDTO icDTO, String referenceNo) {
        Optional<ICCashRemittanceData> optionalICCashRemittanceData = icCashRemittanceDataService.findByExchangeCodeAndReferenceNoAndProcessStatus(icDTO.getExchangeCode(), referenceNo, 0, RemittanceDataStatus.OPEN);
        if (optionalICCashRemittanceData.isPresent()) {
            paymentApiResponse = notifyStatus(paymentApiResponse, icDTO, optionalICCashRemittanceData.get(), referenceNo);
        } else {
            paymentApiResponse.setErrorMessage(Constants.REFERENCE_NOT_EXIST);
            paymentApiResponse.setApiStatus(Constants.API_STATUS_INVALID);
        }
        return paymentApiResponse;
    }

    public PaymentApiResponse notifyStatus(PaymentApiResponse paymentApiResponse, ICExchangePropertyDTO icDTO, ICCashRemittanceData icCashRemittanceData, String referenceNo) {
        if (ApiUtil.validateIfICPropertiesIsNotExist(icDTO, icDTO.getNotifyRemStatusUrl())) {
            logger.error(Constants.EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_NOTIFY_STATUS);
            paymentApiResponse.setErrorMessage(Constants.EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_NOTIFY_STATUS);
            paymentApiResponse.setApiStatus(Constants.API_STATUS_ERROR);
            return paymentApiResponse;
        }

        String status = "";
        String response = "";
        String newStatus = getStatus(icCashRemittanceData.getProcessStatus());
        ICConfirmDTO icConfirmDTO = ICConfirmDTO.builder().reference(referenceNo).newStatus(newStatus).remarks("Success").build();
        String uuid = UUID.randomUUID().toString();
        try {
            HttpEntity<ICConfirmDTO> httpEntity = ApiUtil.createHttpEntity(icConfirmDTO, uuid, icDTO);
            ResponseEntity<ICConfirmResponseDTO> responseEntity = restTemplate.exchange(icDTO.getNotifyRemStatusUrl(), HttpMethod.POST, httpEntity, ICConfirmResponseDTO.class);
            response = convertObjectToString(responseEntity.getBody());
            icCashRemittanceData.setApiResponse(response);
            icCashRemittanceData.setMiddlewarePush(Constants.MIDDLEWARE_PUSH_DONE);
            icCashRemittanceData.setProcessStatus(RemittanceDataStatus.COMPLETED);

            saveRemittanceData(icCashRemittanceData, icDTO.getExchangeCode());

            status = Constants.API_STATUS_VALID;
            logger.info("Execute notify for ReferenceNo: {}", referenceNo);
        } catch (Exception e) {
            status = Constants.API_STATUS_ERROR;
            response = convertObjectToString(e.getMessage());
            paymentApiResponse.setErrorMessage(response);
            logger.error("Error in notify for ReferenceNo: {},  ERROR: {}", referenceNo, e.getMessage());
        }
        paymentApiResponse.setApiStatus(status);
//        apiTraceService.saveApiTrace(icDTO.getExchangeCode(), convertObjectToString(icConfirmDTO), response, status, Constants.REQUEST_TYPE_NOTIFY_REM_STATUS, uuid);
        return paymentApiResponse;
    }

    public RemittanceData saveRemittanceData(ICCashRemittanceData icCashRemittanceData, String exchangeCode) {
        RemittanceData remittanceData = new ObjectMapper().convertValue(icCashRemittanceData, RemittanceData.class);
        remittanceProcessService.saveWebOrSpotData(remittanceData, exchangeCode, Constants.EXCHANGE_HOUSE_INSTANT_CASH);
//        icCashRemittanceDataService.save(icCashRemittanceData);
        return remittanceData;
    }

    private void updateNotifiedICRemittanceData(ResponseEntity<ICConfirmResponseDTO> responseEntity, RemittanceData remittanceData) {
        remittanceData.setApiResponse(convertObjectToString(responseEntity.getBody()));
        if (HttpStatus.OK.equals(responseEntity.getStatusCode()) && Objects.nonNull(responseEntity.getBody())) {
            logger.info("Notify Response data:" + responseEntity.getBody());
            remittanceData.setMiddlewarePush(Constants.MIDDLEWARE_PUSH_DONE);
        } else {
            if (remittanceData.getTryCount() >= Constants.TRY_COUNT) {
                remittanceData.setMiddlewarePush(Constants.MIDDLEWARE_PUSH_ERROR);
            } else {
                remittanceData.setTryCount(remittanceData.getTryCount() + 1);
            }
            logger.error("updateNotifiedICRemittanceData() API Responded with different status code: " + responseEntity.getStatusCode());
        }
    }

    public List<RemittanceData> findAllToNotifyPaidOrRejectedByExchangeCode(String exchangeCode) {
        return remittanceDataService.findAllByExchangeCodeAndMiddlewarePushAndSourceTypeAndProcessStatuses(exchangeCode, 0, Constants.API_SOURCE_TYPE, new ArrayList<>(Arrays.asList(STATUS_COMPLETED, STATUS_REJECTED)));
    }

    private String getStatus(String status) {
        if (Objects.nonNull(status)) {
            if (status.equals(STATUS_COMPLETED)) {
                return NEW_STATUS_Y;
            } else if (status.equals(STATUS_REJECTED)) {
                return NEW_STATUS_X;
            }
        }
        return NEW_STATUS_D;
    }


}
