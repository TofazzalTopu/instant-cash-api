package com.info.api.service.ic;


import com.info.api.dto.ic.APIResponse;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.ic.ICPaymentStatusDTO;

import javax.validation.constraints.NotNull;

public interface ICRetrievePaymentStatusService<T> {

    APIResponse<T> getPaymentStatus(@NotNull ICExchangePropertyDTO dto, @NotNull String referenceNo);

}
