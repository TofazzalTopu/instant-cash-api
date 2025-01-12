package com.info.api.service.impl.ic;

import com.info.api.entity.ICCashRemittanceData;
import com.info.api.repository.ICCashRemittanceDataRepository;
import com.info.api.service.ic.ICCashRemittanceDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ICCashRemittanceDataServiceImpl implements ICCashRemittanceDataService {

    private final ICCashRemittanceDataRepository icCashRemittanceDataRepository;

    public ICCashRemittanceDataServiceImpl(ICCashRemittanceDataRepository icCashRemittanceDataRepository) {
        this.icCashRemittanceDataRepository = icCashRemittanceDataRepository;
    }

    @Override
    public ICCashRemittanceData save(ICCashRemittanceData remittanceData) {
        return icCashRemittanceDataRepository.save(remittanceData);
    }

    @Override
    public Optional<ICCashRemittanceData> findByExchangeCodeAndReferenceNoAndProcessStatus(String exchangeCode, String referenceNo, Integer middlewarePush, String processStatus) {
        return icCashRemittanceDataRepository.findByExchangeCodeAndReferenceNoAndMiddlewarePushAndProcessStatus(exchangeCode, referenceNo, middlewarePush, processStatus);
    }

    @Override
    public Optional<ICCashRemittanceData> findByExchangeCodeAndReferenceNo(String exchangeCode, String referenceNo) {
        return icCashRemittanceDataRepository.findByExchangeCodeAndReferenceNo(exchangeCode, referenceNo);
    }


}
