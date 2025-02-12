package com.info.api.service.ic;

import com.info.api.dto.ic.ICExchangePropertyDTO;

import javax.validation.constraints.NotNull;

public interface ICUnlockRemittanceService {
    void unlockICOutstandingRemittance(ICExchangePropertyDTO icExchangePropertyDTO);
    String unlockICOutstandingRemittance(@NotNull String referenceNo, @NotNull ICExchangePropertyDTO icExchangePropertyDTO);

}
