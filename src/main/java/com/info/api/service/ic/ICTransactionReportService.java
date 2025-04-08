package com.info.api.service.ic;

import com.info.api.dto.ic.APIResponse;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.dto.ic.TransactionReportRequestBody;

import javax.validation.constraints.NotNull;

public interface ICTransactionReportService<T> {

    APIResponse<T> fetchICTransactionReport(@NotNull ICExchangePropertyDTO icExchangePropertyDTO, @NotNull TransactionReportRequestBody report);


}
