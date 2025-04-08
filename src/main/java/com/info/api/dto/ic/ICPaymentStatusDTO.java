package com.info.api.dto.ic;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ICPaymentStatusDTO implements Serializable {

    private static final long serialVersionUID = -7969298090989117693L;

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
    private String sentAt;

}
