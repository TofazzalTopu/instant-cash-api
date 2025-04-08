package com.info.api.service.ic;


import com.info.api.dto.ic.APIResponse;
import com.info.api.dto.ic.ICExchangePropertyDTO;

import javax.validation.constraints.NotNull;

public interface ICRetrievePaymentStatusService {

    APIResponse getPaymentStatus(@NotNull ICExchangePropertyDTO dto, @NotNull String referenceNo);

}
