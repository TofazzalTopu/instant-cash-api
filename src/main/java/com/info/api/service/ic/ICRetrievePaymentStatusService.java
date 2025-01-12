package com.info.api.service.ic;

import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.ic.ICPaymentStatusDTO;

import javax.validation.constraints.NotNull;

public interface ICRetrievePaymentStatusService {

    ICPaymentStatusDTO retrievePaymentStatus(@NotNull String referenceNo, @NotNull ICExchangePropertyDTO dto);

}
