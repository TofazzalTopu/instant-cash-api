package com.info.api.dto.ic;


import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ICConfirmDTO {

    @NotNull
    private String reference;

    @NotNull
    private String newStatus;

    private String remarks;

    private ICConfirmBeneficiaryDTO beneficiaryDetails;
}
