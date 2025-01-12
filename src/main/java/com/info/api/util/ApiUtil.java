package com.info.api.util;

import com.info.api.entity.ExchangeHouseProperty;
import com.info.api.dto.instantCash.ICExchangePropertyDTO;
import com.info.api.service.common.LoadExchangeHouseProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ApiUtil {

    public static final Logger logger = LoggerFactory.getLogger(ApiUtil.class);

    public final static String IC_OUTSTANDING_API = "IC_OUTSTANDING_API";
    public final static String IC_CONFIRM_API = "IC_CONFIRM_API";
    public final static String IC_STATUS_API = "IC_STATUS_API";
    public final static String IC_UNLOCK_API = "IC_UNLOCK_API";
    public final static String IC_RECEIVE_PAYMENT_API = "IC_RECEIVE_PAYMENT_API";
    public final static String IC_SUB_KEY_PRIMARY = "IC_OCP_APIM_SUB_KEY_PRIMARY";
    public final static String IC_AGENT_ID = "IC_AGENT_ID";
    public final static String IC_PAYMENT_API_AGENT_ID = "IC_PAYMENT_API_AGENT_ID";

    public static HttpHeaders createHeader(String correlationId, String apiFinancialId, String apiKey, String password) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.set("x-fapi-financial-id", apiFinancialId);
            headers.set("Authorization", "Basic " + password);
            headers.set("x-correlation-id", correlationId);
            headers.set("x-idempotency-key", UUID.randomUUID().toString());
            headers.set("Ocp-Apim-Subscription-Key", apiKey);
            headers.set("Content-Type", "application/json");
        } catch (Exception e) {
            logger.error("Error in createHeader. Error = " + e);
        }
        return headers;
    }



    public static <T> HttpEntity<T> createHttpEntity(T body, String correlationId, String apiFinancialId, String apiKey, String password) {
        return new HttpEntity<>(body, createHeader(correlationId, apiFinancialId, apiKey, password));
    }

    public static <T> HttpEntity<T> createHttpEntity(T body, String correlationId, ICExchangePropertyDTO dto) {
        return new HttpEntity<>(body, createHeader(correlationId, dto.getAgentId(), dto.getOcpApimSubKey(), dto.getPassword()));
    }

    public static boolean isNonNull(ExchangeHouseProperty property, final String key, final String agentId, final String password) {
        return Objects.nonNull(property.getExchangeCode()) && Objects.nonNull(property.getKeyLabel()) && Objects.nonNull(property.getKeyValue()) && Objects.nonNull(key) && Objects.nonNull(agentId) && Objects.nonNull(password);
    }

    public static boolean validateIfICPropertiesIsNotExist(ICExchangePropertyDTO dto, String url) {
        return Objects.isNull(url) || Objects.isNull(dto.getExchangeCode()) || Objects.isNull(dto.getOcpApimSubKey()) || Objects.isNull(dto.getAgentId()) || Objects.isNull(dto.getPassword());
    }

    public static void validateICExchangePropertiesBeforeProceed(ICExchangePropertyDTO dto, String url, String message) {
        if (validateIfICPropertiesIsNotExist(dto, url)) {
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    public static ICExchangePropertyDTO getICExchangeProperties() {
        List<ExchangeHouseProperty> exchangeHousePropertyList = LoadExchangeHouseProperty.getICExchangeHouseProperty();
        ICExchangePropertyDTO icDto = new ICExchangePropertyDTO();
        exchangeHousePropertyList.forEach(e -> {
            icDto.setExchangeCode(e.getExchangeCode());
            switch (e.getKeyLabel()) {
                case IC_AGENT_ID:
                    icDto.setAgentId(e.getKeyValue());
                    break;
                case IC_SUB_KEY_PRIMARY:
                    icDto.setOcpApimSubKey(e.getKeyValue());
                    break;
                case IC_OUTSTANDING_API:
                    icDto.setOutstandingUrl(e.getKeyValue());
                    break;
                case IC_CONFIRM_API:
                    icDto.setNotifyRemStatusUrl(e.getKeyValue());
                    break;
                case IC_RECEIVE_PAYMENT_API:
                    icDto.setPaymentReceiveUrl(e.getKeyValue());
                    break;
                case IC_UNLOCK_API:
                    icDto.setUnlockUrl(e.getKeyValue());
                    break;
                case IC_PAYMENT_API_AGENT_ID:
                    icDto.setPaymentAgentId(e.getKeyValue());
                    break;
                case IC_STATUS_API:
                    icDto.setStatusUrl(e.getKeyValue());
                    break;
            }
        });
        return icDto;
    }


}
