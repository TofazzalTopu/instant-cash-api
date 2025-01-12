package com.info.api.service.common;

import com.info.api.entity.RemittanceData;

import java.util.Date;
import java.util.List;

public interface RemittanceProcessService {

    List<RemittanceData> saveAllRemittanceData(List<RemittanceData> remittanceDataList);

    void processDownloadData(List<RemittanceData> remittanceDataList, String exchangeCode, String exchangeName);


    void saveWebOrSpotData(RemittanceData data, String exchangeCode, String exchangeName);

}
