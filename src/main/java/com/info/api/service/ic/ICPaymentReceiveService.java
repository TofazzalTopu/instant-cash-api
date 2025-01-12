package com.info.api.service.ic;

import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.SearchApiResponse;

import javax.validation.constraints.NotNull;

public interface ICPaymentReceiveService {

    SearchApiResponse paymentReceive(@NotNull ICExchangePropertyDTO dto, @NotNull String referenceNo);

}
