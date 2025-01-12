package com.info.api.service.common;

import com.info.api.entity.RemittanceData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CommonService {

    Map<String, String> getAccountBranchInfo(String accountNo);

    Date getCurrentBusinessDate();

    boolean isAuthorizedRequest(String userId, String password);
    void verifyAuthorization(String userId, String password);


    List<RemittanceData> getRemittanceDataForNotification(String exchangeCode);
}
