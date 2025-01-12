package com.info.api.service.impl.common;

import com.info.api.entity.RemittanceData;
import com.info.api.repository.CommonRepository;
import com.info.api.service.common.CommonService;
import com.info.api.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {

    public static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    private final CommonRepository commonRepository;

    public CommonServiceImpl(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }


    @Override
    public Map<String, String> getAccountBranchInfo(String accountNo) {
        return commonRepository.getAccountBranchInfo(accountNo);
    }

    public Date getCurrentBusinessDate() {
        Timestamp date = commonRepository.getCurrentBusinessDate();
        return DateUtil.convertTimestampToCalendar(date).getTime();
    }

    @Override
    public boolean isAuthorizedRequest(String userId, String password) {
        boolean result = false;
        try {
            result = commonRepository.isUserExistByUserIdAndPassword(userId, password);
        } catch (Exception e) {
            logger.error("Error in isAuthorizedRequest: {} ", e.getMessage());
        }
        return result;
    }

    @Override
    public void verifyAuthorization(String userId, String password) {
        if (!isAuthorizedRequest(userId, password)) {
            throw new RuntimeException("User is not authorised to proceed the request!.");
        }
    }

    @Override
    public List<RemittanceData> getRemittanceDataForNotification(String exchangeCode) {
        return commonRepository.getRemittanceDataForNotification(exchangeCode);
    }

}
