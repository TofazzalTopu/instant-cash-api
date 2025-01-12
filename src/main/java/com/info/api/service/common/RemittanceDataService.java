package com.info.api.service.common;


import com.info.api.entity.RemittanceData;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RemittanceDataService {

    RemittanceData save(RemittanceData remittanceData);

    int update(String status, String referenceNo);

    List<RemittanceData> saveAll(List<RemittanceData> remittanceDataList);

    List<String> findAllByExchangeCodeAndReferenceDateAndReferenceNumbers(String exchangeCode, Date referenceDate, List<String> referenceNumbers);
    List<String> findAllByExchangeCodeAndReferenceNumbers(String exchangeCode, List<String> referenceNumbers);

    Optional<RemittanceData> findByExchangeCodeAndReferenceNo(String exchangeCode, String referenceNo);

    List<RemittanceData> findAllByExchangeCodeAndReferenceNo(String exchangeCode, String referenceNo);

    List<RemittanceData> findAllByExchangeCodeAndMiddlewarePushAndSourceTypeAndProcessStatuses(String exchangeCode, Integer middlewarePush, String sourceType, List<String> processStatuses);

    List<RemittanceData> processAndSaveRemittanceData(List<RemittanceData> listData, String exchangeCode, String exchangeName);

    List<RemittanceData> findAllByExchangeCodeAndSourceTypeAndProcessStatus(String exchangeCode, String sourceType, String status);


}
