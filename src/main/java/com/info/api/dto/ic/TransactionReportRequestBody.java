package com.info.api.dto.ic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReportRequestBody {

    @NotNull
    private String userId;
    @NotNull
    private String password;
    @NotNull
    private String bruserid;
    @NotNull
    private String brcode;
    @NotNull
    private String exchcode;
    @NotNull
    private String fromDate;
    @NotNull
    private String toDate;
    private int pageNumber;
    private int pageSize;


}
