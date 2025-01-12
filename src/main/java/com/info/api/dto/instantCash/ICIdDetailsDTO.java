package com.info.api.dto.instantCash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ICIdDetailsDTO {

    /**
     * Mandatory if receiving country is Tanzania.
     * The value will be displayed only to the requested partners.
     */
    private String type;

    /**
     * Mandatory if receiving country is Tanzania.
     * The value will be displayed only to the requested partners.
     */
    private String number;
    /**
     Format: ISO-3166 alpha-2 code
     */
    private Date issueDate;
    /**
     Format: ISO-3166 alpha-2 code
     */
    private Date expiryDate;
    /**
     Format: ISO-3166 alpha-2 code
     */
    private String placeOfIssue;
}
