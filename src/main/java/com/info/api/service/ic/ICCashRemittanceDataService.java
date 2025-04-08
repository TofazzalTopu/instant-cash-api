package com.info.api.service.ic;

import com.info.api.entity.ICCashRemittanceData;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface ICCashRemittanceDataService {

    ICCashRemittanceData save(@NotNull ICCashRemittanceData remittanceData);

    Optional<ICCashRemittanceData> findByExchangeCodeAndReferenceNoAndProcessStatus(@NotNull String exchangeCode, @NotNull String referenceNo, @NotNull Integer middlewarePush, @NotNull String processStatus);

    Optional<ICCashRemittanceData> findByExchangeCodeAndReferenceNo(@NotNull String exchangeCode, @NotNull String referenceNo);


    void delete(ICCashRemittanceData icCashRemittanceData);
}
