package com.info.api.service.instantCash;

import com.info.api.entity.RemittanceData;
import com.info.api.dto.instantCash.ICExchangePropertyDTO;

import java.util.List;

public interface ICOutstandingRemittanceService {

    List<RemittanceData> fetchICOutstandingRemittance(ICExchangePropertyDTO icExchangePropertyDTO);


}
