package com.info.api.mapper;

import com.info.api.constants.RemittanceDataStatus;
import com.info.api.entity.ApiTrace;
import com.info.api.entity.RemittanceData;
import com.info.api.dto.ic.*;
import com.info.api.service.common.CommonService;
import com.info.api.util.Constants;
import com.info.api.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ICOutstandingRemittanceMapper {

    public static final Logger logger = LoggerFactory.getLogger(ICOutstandingRemittanceMapper.class);

    private final CommonService commonService;

    @Value("${bank.code}")
    private String bankCode;


    public ICOutstandingRemittanceMapper(CommonService commonService) {
        this.commonService = commonService;
    }


    public RemittanceData prepareRemittanceData(ICOutstandingTransactionDTO icOutstandingDTO, String exchangeCode, ApiTrace trace) {
        RemittanceData remittanceData = new RemittanceData();
        try {
            DateFormat dt = new SimpleDateFormat("yyyyMMdd");
            remittanceData.setAmount(Objects.nonNull(icOutstandingDTO.getPayingAmount()) ? icOutstandingDTO.getPayingAmount() : icOutstandingDTO.getSettlementAmount());
            remittanceData.setAmountOriginate(icOutstandingDTO.getSettlementAmount());
            remittanceData.setPaidBy(icOutstandingDTO.getPayingAgentName());
            remittanceData.setCharges(icOutstandingDTO.getCommissionAmount());
            remittanceData.setPaidBranch(icOutstandingDTO.getMessagePayeeBranch());

            mapBeneficiaryInfo(remittanceData, icOutstandingDTO);

            remittanceData.setExchangeCode(exchangeCode);
            remittanceData.setExchangeName(Constants.EXCHANGE_HOUSE_INSTANT_CASH);
            remittanceData.setExchangeTransactionDate(icOutstandingDTO.getSentAt());
            remittanceData.setExchangeTransactionNo(icOutstandingDTO.getPartnerReference());
            remittanceData.setCountryOriginate(icOutstandingDTO.getOriginatingCountry());
            remittanceData.setPurpose(icOutstandingDTO.getRemittancePurpose());
            remittanceData.setReferenceDate(dt.parse(icOutstandingDTO.getSentAt()));
            remittanceData.setReferenceNo(icOutstandingDTO.getReference());
            remittanceData.setSecurityCode(icOutstandingDTO.getPartnerReference()); // data not match
            remittanceData.setCurrencyOriginate(icOutstandingDTO.getPayingCurrency());
            remittanceData.setExchangeRate(icOutstandingDTO.getSettlementRate());

            mapRemitterInfo(remittanceData, icOutstandingDTO);
            mapBankBranchInfo(remittanceData, icOutstandingDTO);

            remittanceData.setProcessStatus(RemittanceDataStatus.OPEN);
            remittanceData.setProcessDate(trace.getCbsDate());
            remittanceData.setMiddlewareId(trace.getId());
            remittanceData.setMiddlewarePush(Constants.MIDDLEWARE_PUSH_UNDONE);
            remittanceData.setSourceType(Constants.API_SOURCE_TYPE);
        } catch (Exception e) {
            logger.error("Error in prepareRemittanceData for TraceID: " + trace.getId(), e);
        }
        return remittanceData;
    }

    private void mapBeneficiaryInfo(RemittanceData remittanceData, ICOutstandingTransactionDTO icOutstandingDTO) {
        try {
            if (Objects.isNull(icOutstandingDTO.getBeneficiary())) return;

            ICBeneficiaryDTO beneficiary = icOutstandingDTO.getBeneficiary();
            remittanceData.setCreditorName(buildFullName(beneficiary.getFirstName(), beneficiary.getMiddleName(), beneficiary.getLastName()));
            remittanceData.setPhoneNo(getNonNullValue(beneficiary.getPhoneNumber(), beneficiary.getMobileNumber()));

            if (Objects.nonNull(beneficiary.getAddress())) {
                ICAddressDTO address = beneficiary.getAddress();
                remittanceData.setCityDistrict(buildFullName(address.getCity(), address.getDistrict()));
                remittanceData.setReceiverAddress(buildFullName(address.getAddressLine1(), address.getAddressLine2()));
            }
        } catch (Exception e) {
            logger.error("Error in mapBeneficiaryInfo() {} ", e.getMessage());
        }
    }

    private void mapRemitterInfo(RemittanceData remittanceData, ICOutstandingTransactionDTO icOutstandingDTO) {
        if (Objects.isNull(icOutstandingDTO.getRemitter())) return;

        ICRemitterDTO remitter = icOutstandingDTO.getRemitter();
        remittanceData.setSenderPhone(getNonNullValue(remitter.getPhoneNumber(), remitter.getMobileNumber()));
        remittanceData.setSenderName(buildFullName(remitter.getFirstName(), remitter.getMiddleName(), remitter.getLastName()));

        if (Objects.nonNull(remitter.getAddress())) {
            ICAddressDTO address = remitter.getAddress();
            remittanceData.setSenderAddress(buildFullName(address.getAddressLine1() + " " + address.getAddressLine2()));
        }

        if (Objects.nonNull(remitter.getIdDetails())) {
            remittanceData.setIdNo(remitter.getIdDetails().getNumber());
            remittanceData.setSenderIdType(remitter.getIdDetails().getType());
        }

    }

    private void mapBankBranchInfo(RemittanceData remittanceData, ICOutstandingTransactionDTO icOutstandingDTO) {
        try {
            boolean isBankDetailsNonEmpty = Objects.nonNull(icOutstandingDTO.getBeneficiary()) && Objects.nonNull(icOutstandingDTO.getBeneficiary().getBankDetails());
            if (!isBankDetailsNonEmpty) return;

            ICBankDetailsDTO bankDetails = icOutstandingDTO.getBeneficiary().getBankDetails();
            if (isEFTRemittance(bankDetails.getBankCode())) {
                remittanceData.setRemittanceMessageType("EFT");
                remittanceData.setBankCode(bankCode);
                // For 13 digit account number replacing the branch information with account branch
                if (Objects.nonNull(remittanceData.getCreditorAccountNo()) && remittanceData.getCreditorAccountNo().length() == 13) {
                    Map<String, String> accountBranchInfo = commonService.getAccountBranchInfo(remittanceData.getCreditorAccountNo());
                    remittanceData.setBranchCode(Integer.parseInt(accountBranchInfo.get("branch_code")));
                    remittanceData.setBranchName(accountBranchInfo.get("branch_name"));
                    remittanceData.setBranchRoutingNumber(bankCode + StringUtil.padLeftZeros(accountBranchInfo.get("routing_no"), 6));
                    remittanceData.setOwnBranchCode(Integer.parseInt(accountBranchInfo.get("branch_code")));
                }
            } else {
                remittanceData.setRemittanceMessageType("BEFTN");
                remittanceData.setBranchRoutingNumber(bankDetails.getBankCode());
                String bankName = buildFullName(bankDetails.getBankName(), bankDetails.getBankAddress1(), bankDetails.getBankAddress2());
                remittanceData.setBankName(bankName);
                remittanceData.setBankCode(bankDetails.getBankCode());
                remittanceData.setBranchName(bankName);
            }
        } catch (Exception e) {
            logger.error("Error in mapBankBranchInfo() {}", e.getMessage());
        }
    }

    private boolean isEFTRemittance(String rmBankCode) {
        return Objects.nonNull(rmBankCode) && rmBankCode.trim().length() >= 3 && rmBankCode.trim().substring(0, 3).equals(bankCode);
    }

    private String buildFullName(String... names) {
        return Arrays.stream(names)
                .filter(Objects::nonNull)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.joining(" "));
    }

    private String getNonNullValue(String phoneNumber, String mobileNumber) {
        return Objects.nonNull(phoneNumber) ? phoneNumber : mobileNumber;
    }


}
