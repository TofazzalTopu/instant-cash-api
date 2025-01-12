package com.info.api.dto.instantCash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ICAddressDTO {

    @NotEmpty
    private String addressLine1;
    private String addressLine2;
    private String district;
    @NotEmpty
    private String city;
    private String postCode;
    private String state;

    private String country;
}
