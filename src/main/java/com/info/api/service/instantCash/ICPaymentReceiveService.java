package com.info.api.service.instantCash;

import com.info.api.dto.instantCash.ICExchangePropertyDTO;
import com.info.api.dto.SearchApiResponse;

import javax.validation.constraints.NotNull;

public interface ICPaymentReceiveService {

    SearchApiResponse paymentReceive(@NotNull ICExchangePropertyDTO dto, @NotNull String referenceNo);

}
