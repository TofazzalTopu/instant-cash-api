package com.info.api.service.ic;

import com.info.api.entity.RemittanceData;
import com.info.api.dto.ic.ICExchangePropertyDTO;

import java.util.List;

public interface ICOutstandingRemittanceService {

    List<RemittanceData> fetchICOutstandingRemittance(ICExchangePropertyDTO icExchangePropertyDTO);


}
