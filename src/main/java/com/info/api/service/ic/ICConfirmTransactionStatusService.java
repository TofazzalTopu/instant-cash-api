package com.info.api.service.ic;

import com.info.api.dto.PaymentApiRequest;
import com.info.api.dto.PaymentApiResponse;
import com.info.api.dto.ic.ICConfirmDTO;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.entity.RemittanceData;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface ICConfirmTransactionStatusService {

    List<RemittanceData> confirmOutstandingTransactionStatus(@NotNull ICExchangePropertyDTO icExchangePropertyDTO, @NotEmpty List<RemittanceData> remittanceDataList);

    PaymentApiResponse confirmCahTransactionPayment(@NotNull PaymentApiResponse paymentApiResponse, PaymentApiRequest paymentApiRequest, @NotNull ICExchangePropertyDTO icDTO);

    PaymentApiResponse notifyPaymentStatus(PaymentApiResponse paymentApiResponse, @NotNull ICExchangePropertyDTO icDTO, @NotNull RemittanceData remittanceData, ICConfirmDTO icConfirmDTO, @NotNull String referenceNo, boolean update);
}
