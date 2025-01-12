package com.info.api.repository;

import com.info.api.entity.RemittanceData;
import com.info.api.entity.RemittanceProcessMaster;
import com.info.api.util.Constants;
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
    private static final String JAVAX_PERSISTENCE_CACHE_RETRIEVE_MODE = "javax.persistence.cache.retrieveMode";
    @PersistenceContext
    private EntityManager em;


    public RemittanceProcessMaster getRemitProcMasterByApiData(Date date, String exchangeCode, int api_data,
                                                               String status) {
        RemittanceProcessMaster master = null;
        try {
            logger.info("Entered in getRemitProcMasterByApiData()");
            TypedQuery<RemittanceProcessMaster> query = em.createQuery(
                    "SELECT r FROM RemittanceProcessMaster r  where r.processDate = :process_date and r.exchangeHouseCode = :exchange_code and r.apiData = :api_data and r.processStatus = :status order by r.id desc ",
                    RemittanceProcessMaster.class);
            query.setParameter("process_date", date);
            query.setParameter("exchange_code", exchangeCode);
            query.setParameter("api_data", api_data);
            query.setParameter("status", status);
            query.setHint(JAVAX_PERSISTENCE_CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
            List<RemittanceProcessMaster> resultList = query.getResultList();
            if (!resultList.isEmpty()) {
                master = resultList.get(0);
            }
            return master;
        } catch (Exception ex) {
            logger.error("Error in getRemitProcMasterByApiData()", ex);
            return master;
        }
    }


    public List<RemittanceData> getRemittancebyReference(String referenceNo, String exchangeCode) {
        try {
            TypedQuery<RemittanceData> query = em.createQuery("SELECT r FROM RemittanceData r  where r.referenceNo = :reference_no and r.exchangeCode= :exchange_code ", RemittanceData.class);
            query.setParameter("reference_no", referenceNo);
            query.setParameter("exchange_code", exchangeCode);
            query.setHint(JAVAX_PERSISTENCE_CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
            return query.getResultList();
        } catch (Exception ex) {
            logger.error("Error in getRemittancebyReference()", ex);
            return null;
        }
    }



    public List<RemittanceData> getRemittanceDataForNotification(String exchangeCode) {
        try {
            TypedQuery<RemittanceData> query = em.createQuery("SELECT r FROM RemittanceData r  where r.exchangeCode=:exchangeCode and r.middlewarePush = 0 and r.sourceType=:source_type and r.finalStatus IN ('02', '03', '04', '05','07','08','14','15', '22') and r.processStatus IN ('COMPLETED','REJECTED') and r.remittanceMessageType <> 'WEB' ", RemittanceData.class);
            query.setParameter("exchangeCode", exchangeCode);
            query.setParameter("source_type", Constants.API_SOURCE_TYPE);
            query.setHint(JAVAX_PERSISTENCE_CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
            return query.getResultList();
        } catch (Exception ex) {
            logger.error("Error in getCancelRequestUnprocessData()", ex);
            return new ArrayList<>();
        }
    }




    public boolean isUserExistByUserIdAndPassword(String userId, String password) {
        boolean result = false;
        try {
            Query query;
            String sql = "select count(*) FROM RMS_API_CHANNELS  WHERE CBSAPI_USERID	= ? AND CBSAPI_USER_PASSWORD = ?";
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
