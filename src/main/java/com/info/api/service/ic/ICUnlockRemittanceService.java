package com.info.api.service.ic;


import com.info.api.dto.ic.APIResponse;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.ic.ICUnlockTransactionResponseDTO;

import javax.validation.constraints.NotNull;

public interface ICUnlockRemittanceService {

    APIResponse<String> unlockICOutstandingRemittance(@NotNull String referenceNo, @NotNull ICExchangePropertyDTO icExchangePropertyDTO);

}
