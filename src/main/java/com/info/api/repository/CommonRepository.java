package com.info.api.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Component
@Transactional
public class CommonRepository {
    private static final Logger logger = LoggerFactory.getLogger(CommonRepository.class);
    @PersistenceContext
    private EntityManager em;


    public boolean isUserExistByUserIdAndPassword(String userId, String password) {
        boolean result = false;
        try {
            Query query;
            String sql = "select count(*) FROM RMS_API_CHANNELS WHERE CBSAPI_USERID = ? AND CBSAPI_USER_PASSWORD = ?";
            query = em.createNativeQuery(sql);
            query.setParameter(1, userId);
            query.setParameter(2, password);

            BigDecimal rowCount = (BigDecimal) query.getSingleResult();

            if (rowCount.compareTo(BigDecimal.ZERO) > 0) {
                result = true;
            }

        } catch (Exception ex) {
            logger.error("Error in isUserExistByUserIdAndPassword()", ex);
        }
        return result;
    }

    public Map<String, String> getAccountBranchInfo(String accountNo) {
        logger.info("Enter into getAccountBranchInfo" + accountNo);
        Map<String, String> map = new HashMap<String, String>();
        try {
            Query query;
            String sql = "SELECT M.MBRN_CODE,M.MBRN_NAME,M.MBRN_MICR_CODE FROM IACLINK I,MBRN M WHERE I.IACLINK_ACTUAL_ACNUM = ? AND M.MBRN_CODE = I.IACLINK_BRN_CODE";
            query = em.createNativeQuery(sql);
            query.setParameter(1, accountNo);

            Object[] obj = (Object[]) query.getSingleResult();
            if (obj != null) {
                map.put("branch_code", String.valueOf(obj[0]));
                map.put("branch_name", String.valueOf(obj[1]));
                map.put("routing_no", String.valueOf(obj[2]));
            }
        } catch (Exception ex) {
            logger.error("Error in getAccountBranchInfo()", ex);
            map.put("branch_code", "0");
            map.put("branch_name", "");
            map.put("routing_no", "0");
        }

        return map;
    }

    public Timestamp getCurrentBusinessDate() {
        try {
            Query query;
            String qry = "SELECT PKG_PB_GLOBAL.FN_GET_CURR_BUS_DATE(1) FROM DUAL";
            query = em.createNativeQuery(qry);
            Timestamp timestamp = (Timestamp) query.getSingleResult();
            return timestamp;
        } catch (Exception ex) {
            logger.error("Error in getCurrentBusinessDate()", ex);
        } finally {
            em.clear();
            em.close();
        }
        return null;
    }

}
