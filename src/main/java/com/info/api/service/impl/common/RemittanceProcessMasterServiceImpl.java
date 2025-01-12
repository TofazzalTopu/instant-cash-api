package com.info.api.service.impl.common;

import com.info.api.entity.RemittanceProcessMaster;
import com.info.api.repository.RemittanceProcessMasterRepository;
import com.info.api.service.common.RemittanceProcessMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class RemittanceProcessMasterServiceImpl implements RemittanceProcessMasterService {

    private static final Logger logger = LoggerFactory.getLogger(RemittanceProcessMasterServiceImpl.class);

    private final RemittanceProcessMasterRepository remittanceProcessMasterRepository;

    @Autowired
    public RemittanceProcessMasterServiceImpl(RemittanceProcessMasterRepository remittanceProcessMasterRepository) {
        this.remittanceProcessMasterRepository = remittanceProcessMasterRepository;
    }

    @Override
    public RemittanceProcessMaster save(RemittanceProcessMaster master) {
        return remittanceProcessMasterRepository.save(master);
    }

    @Override
    public RemittanceProcessMaster findFirstByProcessDateAndExchangeHouseCodeAndApiDataAndProcessStatus(Date date, String exchangeCode, int api_data, String processStatus) {
        return remittanceProcessMasterRepository.findFirstByProcessDateAndExchangeHouseCodeAndApiDataAndProcessStatusOrderByIdDesc(date, exchangeCode, api_data, processStatus);
    }
}
