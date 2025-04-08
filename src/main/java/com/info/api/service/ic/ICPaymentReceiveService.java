package com.info.api.service.ic;


import com.info.api.dto.SearchApiRequest;
import com.info.api.dto.SearchApiResponse;
import com.info.api.dto.ic.ICExchangePropertyDTO;

import javax.validation.constraints.NotNull;

public interface ICPaymentReceiveService {

    SearchApiResponse paymentReceive(@NotNull ICExchangePropertyDTO dto, @NotNull SearchApiRequest apiRequest);

}
