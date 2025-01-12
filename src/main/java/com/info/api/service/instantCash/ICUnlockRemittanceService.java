package com.info.api.service.instantCash;

import com.info.api.dto.instantCash.ICExchangePropertyDTO;

import javax.validation.constraints.NotNull;

public interface ICUnlockRemittanceService {
    void unlockICOutstandingRemittance(ICExchangePropertyDTO icExchangePropertyDTO);
    String unlockICOutstandingRemittance(@NotNull String referenceNo, @NotNull ICExchangePropertyDTO icExchangePropertyDTO);

}
