package com.info.api.service.impl.common;

import com.info.api.entity.RemittanceData;
import com.info.api.entity.RemittanceProcessMaster;
import com.info.api.repository.CommonRepository;
import com.info.api.service.common.ApiTraceService;
import com.info.api.service.common.RemittanceDataService;
import com.info.api.service.common.RemittanceProcessMasterService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RemittanceDataProcessServiceImpl {

    private final ApiTraceService apiTraceService;
    private final CommonRepository commonRepository;

    private final RemittanceDataService remittanceDataService;

    private final RemittanceProcessMasterService remittanceProcessMasterService;

    public RemittanceDataProcessServiceImpl(ApiTraceService apiTraceService, CommonRepository commonRepository, @Lazy RemittanceDataService remittanceDataService, RemittanceProcessMasterService remittanceProcessMasterService) {
        this.apiTraceService = apiTraceService;
        this.commonRepository = commonRepository;
        this.remittanceDataService = remittanceDataService;
        this.remittanceProcessMasterService = remittanceProcessMasterService;
    }


    @Transactional
    public List<RemittanceData> processDownloadData(List<RemittanceData> remittanceDataList, String exchangeCode, String exchangeName) {
        List<RemittanceData> remittanceDataArrayList = new ArrayList<>();
        if (!remittanceDataList.isEmpty()) {
            RemittanceProcessMaster master = null;
            RemittanceProcessMaster masterRecord = commonRepository.getRemitProcMasterByApiData(remittanceDataList.get(0).getProcessDate(),
                    exchangeCode, 1, "OPEN");

            if (masterRecord != null) {
                master = masterRecord;
            }
            long eftCount = remittanceDataList.stream().filter(d -> d.getRemittanceMessageType().equals("EFT")).count();
            long beftnCount = remittanceDataList.stream().filter(d -> d.getRemittanceMessageType().equals("BEFTN")).count();
            long mobileCount = remittanceDataList.stream().filter(d -> d.getRemittanceMessageType().equals("MOBILE")).count();

            if (master == null) {
                master = new RemittanceProcessMaster();
                master.setCommon(true);
                master.setExchangeHouseCode(exchangeCode);
                master.setFileName(exchangeName);
                master.setApiData(1);
                master.setManualOpen(0);
                master.setProcessByUser("CBSRMS");
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
            long time = System.currentTimeMillis();
            System.out.println("Saving started...");
            remittanceDataArrayList = remittanceDataService.saveAll(remittanceDataList);
            long milis = System.currentTimeMillis() - time;
            System.out.println("Total Time: " + TimeUnit.MILLISECONDS.toSeconds(milis));
        }
        return remittanceDataArrayList;
    }

    @Transactional
    public void updateRemittanceData(RemittanceData data) {
        remittanceDataService.save(data);
    }

    @Transactional
    public List<RemittanceData> saveAll(List<RemittanceData> remittanceDataList){
        return remittanceDataService.saveAll(remittanceDataList);
    }



}
