package com.info.api.service.instantCash;

import com.info.api.dto.instantCash.ICExchangePropertyDTO;
import com.info.api.dto.instantCash.ICPaymentStatusDTO;

import javax.validation.constraints.NotNull;

public interface ICRetrievePaymentStatusService {

    ICPaymentStatusDTO retrievePaymentStatus(@NotNull String referenceNo, @NotNull ICExchangePropertyDTO dto);

}
