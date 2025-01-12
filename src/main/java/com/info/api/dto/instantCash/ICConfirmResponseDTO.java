package com.info.api.dto.instantCash;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class ICConfirmResponseDTO {

    @NotNull
    private String reference;

    @NotNull
    private String status;

    @NotNull
    private String statusDescription;

}
