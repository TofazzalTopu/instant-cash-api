package com.info.api.service.impl.common;

import com.info.api.entity.RemittanceData;
import com.info.api.repository.RemittanceDataRepository;
import com.info.api.service.common.RemittanceDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RemittanceDataServiceImpl implements RemittanceDataService {

    private static final Logger logger = LoggerFactory.getLogger(RemittanceDataServiceImpl.class);

    private final RemittanceDataRepository remittanceDataRepository;
    private final RemittanceDataProcessServiceImpl remittanceDataProcessService;

    @Value("${INSTANT_CASH_API_USER_ID}")
    private String INSTANT_CASH_API_USER_ID;

    public RemittanceDataServiceImpl(RemittanceDataRepository remittanceDataRepository, RemittanceDataProcessServiceImpl remittanceDataProcessService) {
        this.remittanceDataRepository = remittanceDataRepository;
        this.remittanceDataProcessService = remittanceDataProcessService;
    }

    @Override
    public RemittanceData save(RemittanceData remittanceData) {
        return remittanceDataRepository.save(remittanceData);
    }

    @Override
    public int update(String status, String referenceNo) {
        return remittanceDataRepository.updateRemittanceStatusByReferenceNo(status, referenceNo);
    }

    @Async
    @Override
    public List<RemittanceData> saveAll(List<RemittanceData> remittanceDataList) {
        return remittanceDataRepository.saveAll(remittanceDataList);
    }


    @Override
    public List<String> findAllByExchangeCodeAndReferenceDateAndReferenceNumbers(String exchangeCode, Date referenceDate, List<String> referenceNumbers) {
        return remittanceDataRepository.findAllByExchangeCodeAndReferenceDateAndReferenceNumbers(exchangeCode, referenceDate, referenceNumbers);
    }

    @Override
    public List<String> findAllByExchangeCodeAndReferenceNumbers(String exchangeCode, List<String> referenceNumbers) {
        return remittanceDataRepository.findAllByExchangeCodeAndReferenceNumbers(exchangeCode, referenceNumbers);
    }

    @Override
    public Optional<RemittanceData> findByExchangeCodeAndReferenceNo(String exchangeCode, String referenceNo) {
        return remittanceDataRepository.findByExchangeCodeAndReferenceNo(exchangeCode, referenceNo);
    }

    @Override
    public List<RemittanceData> findAllByExchangeCodeAndReferenceNo(String exchangeCode, String referenceNo) {
        return remittanceDataRepository.findAllByExchangeCodeAndReferenceNo(exchangeCode, referenceNo);
    }


    public List<RemittanceData> findAllByExchangeCodeAndMiddlewarePushAndSourceTypeAndProcessStatuses(String exchangeCode, Integer middlewarePush, String sourceType, List<String> processStatuses) {
        return remittanceDataRepository.findAllByExchangeCodeAndMiddlewarePushAndSourceTypeOrProcessStatusIn(exchangeCode, middlewarePush, sourceType, processStatuses);
    }

    @Override
    public List<RemittanceData> processAndSaveRemittanceData(List<RemittanceData> remittanceDataList, String exchangeCode, String exchangeName) {
        return remittanceDataProcessService.processDownloadData(remittanceDataList, exchangeCode, exchangeName);
    }

    @Override
    public List<RemittanceData> findAllByExchangeCodeAndSourceTypeAndProcessStatus(String exchangeCode, String sourceType, String status) {
        return remittanceDataRepository.findAllByExchangeCodeAndSourceTypeAndProcessStatus(exchangeCode, sourceType, status);
    }


}
