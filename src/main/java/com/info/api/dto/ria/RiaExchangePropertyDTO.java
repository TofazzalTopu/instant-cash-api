package com.info.api.dto.ria;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiaExchangePropertyDTO {

    @NotNull
    private String exchangeCode;
    @NotNull
    private String agentId;
    @NotNull
    private String ocpApimSubKey;
    @NotNull
    private String apiVersion;
    @NotNull
    private String downloadableUrl;
    @NotNull
    private String searchUrl;
    @NotNull
    private String paymentUrl;
    @NotNull
    private String cashPickUpCancelUrl;
    @NotNull
    private String notifyRemStatusUrl;
    @NotNull
    private String notifyCancelReqUrl;

}
