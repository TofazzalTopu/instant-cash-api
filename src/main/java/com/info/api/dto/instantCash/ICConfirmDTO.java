package com.info.api.dto.instantCash;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ICConfirmDTO {

    @NotNull
    private String reference;

    @NotNull
    private String newStatus;

    private String remarks;

}
