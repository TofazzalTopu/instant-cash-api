package com.info.api.service.impl.common;

import com.info.api.entity.RemittanceData;
import com.info.api.entity.RemittanceProcessMaster;
import com.info.api.service.common.ApiTraceService;
import com.info.api.service.common.RemittanceDataService;
import com.info.api.service.common.RemittanceProcessMasterService;
import com.info.api.service.common.RemittanceProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RemittanceProcessServiceImpl implements RemittanceProcessService {

    private static final Logger logger = LoggerFactory.getLogger(RemittanceProcessServiceImpl.class);

    private final ApiTraceService apiTraceService;

    private final RemittanceDataService remittanceDataService;

    private final RemittanceProcessMasterService remittanceProcessMasterService;


    @Value("${RIA_EXCHANGE_HOUSE_BRANCH_USER:CBSRMS}")
    private String RIA_EXCHANGE_HOUSE_BRANCH_USER;

    public RemittanceProcessServiceImpl(ApiTraceService apiTraceService, RemittanceDataService remittanceDataService,  RemittanceProcessMasterService remittanceProcessMasterService) {
        this.apiTraceService = apiTraceService;
        this.remittanceDataService = remittanceDataService;
        this.remittanceProcessMasterService = remittanceProcessMasterService;
    }

    @Override
    public List<RemittanceData> saveAllRemittanceData(List<RemittanceData> remittanceDataList) {
        return remittanceDataService.saveAll(remittanceDataList);
    }

    @Override
    @Transactional
    public void processDownloadData(List<RemittanceData> remittanceDataList, String exchangeCode, String exchangeName) {
        if (!remittanceDataList.isEmpty()) {
            RemittanceProcessMaster master = null;
            RemittanceProcessMaster masterRecord = remittanceProcessMasterService.findFirstByProcessDateAndExchangeHouseCodeAndApiDataAndProcessStatus(remittanceDataList.get(0).getProcessDate(), exchangeCode, 1, "OPEN");
            if (masterRecord != null) {
                master = masterRecord;
            }

            long eftCount = remittanceDataList.stream().filter(d -> d.getRemittanceMessageType().equals("EFT")).count();
            long beftnCount = remittanceDataList.stream().filter(d -> d.getRemittanceMessageType().equals("BEFTN")).count();
            long mobileCount = remittanceDataList.stream().filter(d -> d.getRemittanceMessageType().equals("MOBILE")).count();

            if (master == null) {
                master = new RemittanceProcessMaster();
                master.setCommon(true);
                master.setFileName(exchangeName);
                master.setApiData(1);
                master.setManualOpen(0);
                master.setProcessByUser(RIA_EXCHANGE_HOUSE_BRANCH_USER);
                master.setExchangeHouseCode(exchangeCode);
                master.setProcessDate(remittanceDataList.get(0).getProcessDate());
                master.setProcessStatus("OPEN");
                master.setTotalBeftn(beftnCount);
                master.setTotalEft(eftCount);
                master.setTotalMobile(mobileCount);
                master.setTotalSpot(0);
                master.setTotalWeb(0);
            } else {
                master.setTotalBeftn(master.getTotalBeftn() + beftnCount);
                master.setTotalEft(master.getTotalEft() + eftCount);
                master.setTotalMobile(master.getTotalMobile() + mobileCount);
            }
            master = remittanceProcessMasterService.save(master);

            for (RemittanceData data : remittanceDataList) {
                data.setRemittanceProcessMaster(master);
            }
            remittanceDataService.saveAll(remittanceDataList);
        }
    }


    @Override
    public void saveWebOrSpotData(RemittanceData data, String exchangeCode, String exchangeName) {
        logger.info("Enter into saveWebOrSpotData");
        try {
            RemittanceProcessMaster master = null;
            RemittanceProcessMaster masterRecord = remittanceProcessMasterService.findFirstByProcessDateAndExchangeHouseCodeAndApiDataAndProcessStatus(data.getProcessDate(), exchangeCode, 1, "OPEN");
            if (masterRecord != null) master = masterRecord;

            if (master == null) {
                logger.info("Enter into saveWebOrSpotData masterRecord present");
                master = new RemittanceProcessMaster();
                master.setCommon(true);
                master.setExchangeHouseCode(exchangeCode);
                master.setFileName(exchangeName);
                master.setApiData(1);
                master.setManualOpen(0);
                master.setProcessByUser(RIA_EXCHANGE_HOUSE_BRANCH_USER);
                master.setProcessDate(data.getProcessDate());
                master.setProcessStatus("OPEN");
                master.setTotalBeftn(0);
                master.setTotalEft(0);
                master.setTotalMobile(0);
                master.setTotalSpot(0);
                master.setTotalWeb(1);
            } else {
                master.setTotalWeb(master.getTotalWeb() + 1);
            }
            master = remittanceProcessMasterService.save(master);
            data.setRemittanceProcessMaster(master);
            remittanceDataService.save(data);
            apiTraceService.updateSyncFlag(exchangeCode, data.getSecurityCode(), data.getProcessDate());
        } catch (Exception e) {
            logger.info("Error in saveWebOrSpotData. Error = " + e);
        }
    }
}
