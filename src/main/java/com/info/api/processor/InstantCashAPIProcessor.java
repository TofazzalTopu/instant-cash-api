package com.info.api.processor;

import com.info.api.entity.RemittanceData;
import com.info.api.dto.ic.ICExchangePropertyDTO;
import com.info.api.service.common.RemittanceDataService;
import com.info.api.service.ic.ICNotifyPaymentStatusRemittanceService;
import com.info.api.service.ic.ICOutstandingRemittanceService;
import com.info.api.service.ic.ICPaymentReceiveService;
import com.info.api.service.ic.ICUnlockRemittanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.info.api.util.PasswordUtil.generateBase64Hash;

@Component
public class InstantCashAPIProcessor {

    public static final Logger logger = LoggerFactory.getLogger(InstantCashAPIProcessor.class);

    private final RemittanceDataService remittanceDataService;
    private final ICPaymentReceiveService icPaymentReceiveService;

    private final ICUnlockRemittanceService icUnlockRemittanceService;
    private final ICOutstandingRemittanceService icOutstandingRemittanceService;
    private final ICNotifyPaymentStatusRemittanceService icNotifyPaymentStatusRemittanceService;

    @Value("${INSTANT_CASH_API_USER_ID}")
    String icUserId;
    @Value("${INSTANT_CASH_API_USER_RECEIVE_PAYMENT_API_USER_ID}")
    String icPaymentUserId;

    @Value("${INSTANT_CASH_API_USER_PASSWORD}")
    String icPassword;

    public InstantCashAPIProcessor(RemittanceDataService remittanceDataService, ICPaymentReceiveService icPaymentReceiveService, ICUnlockRemittanceService icUnlockRemittanceService, ICOutstandingRemittanceService icOutstandingRemittanceService, ICNotifyPaymentStatusRemittanceService icNotifyPaymentStatusRemittanceService) {
        this.remittanceDataService = remittanceDataService;
        this.icPaymentReceiveService = icPaymentReceiveService;
        this.icUnlockRemittanceService = icUnlockRemittanceService;
        this.icOutstandingRemittanceService = icOutstandingRemittanceService;
        this.icNotifyPaymentStatusRemittanceService = icNotifyPaymentStatusRemittanceService;
    }

    public void process(ICExchangePropertyDTO icExchangePropertyDTO) {
        if (Objects.nonNull(icExchangePropertyDTO)) {
            try {
                icExchangePropertyDTO.setPassword(generateBase64Hash(icUserId, icPassword));
                long time = System.currentTimeMillis();
                List<RemittanceData> remittanceDataList = icOutstandingRemittanceService.fetchICOutstandingRemittance(icExchangePropertyDTO);
                List<RemittanceData> notifiedList = icNotifyPaymentStatusRemittanceService.notifyPaymentStatus(icExchangePropertyDTO);
//                icUnlockRemittanceService.unlockICOutstandingRemittance(icExchangePropertyDTO);

//                System.out.println("remittanceDataList size: " + remittanceDataList.size());
//                long millis = System.currentTimeMillis() - time;
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
//                System.out.println("\nseconds: " + seconds +"\n");
//                Optional<ICCashRemittanceData> icCashRemittanceData = icCashRemittanceDataService.findByExchangeCodeAndReferenceNoAndProcessStatus(paymentApiRequest.getExchCode(), paymentApiRequest.getPinno(), RemittanceData.OPEN);
//                RemittanceData remittanceData = remittanceDataService.saveRemittanceData("1452070054", "000007141", icUserId);
//                SearchApiResponse remittanceData = icPaymentReceiveService.paymentReceive("000007141", icExchangePropertyDTO);
                logger.info("InstantCashAPIProcessor ended.");
            } catch (Exception ex) {
                logger.error(ex.getLocalizedMessage());
            }
        } else {
            logger.info("Instant Cash API Properties is missing. Please add properties in the DB.");
        }
    }


}
