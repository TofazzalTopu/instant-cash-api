package com.info.api.dto.instantCash;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ICPaymentStatusDTO {

    @NotNull
    private String reference;

    @NotNull
    private String status;

    @NotNull
    private String statusDescription;

    @NotNull
    private String payingAgentCorrespondentId;

    @NotNull
    private String payingAgentName;

    @NotNull
    private LocalDateTime sentAt;

}
