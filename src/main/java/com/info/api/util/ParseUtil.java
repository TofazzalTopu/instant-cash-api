package com.info.api.util;

import com.info.api.dto.PaymentApiRequest;
import com.info.api.dto.PaymentApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ParseUtil {

    private ParseUtil() {

    }

    private static final Logger logger = LoggerFactory.getLogger(ParseUtil.class);

    public static PaymentApiResponse mapPaymentApiResponse(PaymentApiResponse paymentApiResponse, PaymentApiRequest paymentApiRequest) {
        paymentApiResponse.setPayoutStatus(null);

        if (Objects.isNull(paymentApiRequest)) {
            paymentApiResponse.setErrorMessage("Request Can not be empty");
        } else if (Objects.isNull(paymentApiRequest.getExchCode()) || paymentApiRequest.getExchCode().isEmpty()) {
            paymentApiResponse.setErrorMessage("Exchange Code can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getPinno()) || paymentApiRequest.getPinno().isEmpty()) {
            paymentApiResponse.setErrorMessage("Pin No can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getBrUserId()) || paymentApiRequest.getBrUserId().isEmpty()) {
            paymentApiResponse.setErrorMessage("Branch User ID can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getBrCode()) || paymentApiRequest.getBrCode().isEmpty()) {
            paymentApiResponse.setErrorMessage("Branch Code can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getBeneIDNumber()) || paymentApiRequest.getBeneIDNumber().isEmpty()) {
            paymentApiResponse.setErrorMessage("Beneficiary ID can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getDob()) || paymentApiRequest.getDob().isEmpty()) {
            paymentApiResponse.setErrorMessage("Date of Birth can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getTranNo()) || paymentApiRequest.getTranNo().isEmpty()) {
            paymentApiResponse.setErrorMessage("Tran No can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getAddress()) || paymentApiRequest.getAddress().isEmpty()) {
            paymentApiResponse.setErrorMessage("Address can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getCity()) || paymentApiRequest.getCity().isEmpty()) {
            paymentApiResponse.setErrorMessage("City can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getMobileNo()) || paymentApiRequest.getMobileNo().isEmpty()) {
            paymentApiResponse.setErrorMessage("Mobile No can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getPurposeOfTran()) || paymentApiRequest.getPurposeOfTran().isEmpty()) {
            paymentApiResponse.setErrorMessage("Purpose of Transaction can not be null or empty");
        } else if (Objects.isNull(paymentApiRequest.getRelationWithRemitter()) || paymentApiRequest.getRelationWithRemitter().isEmpty()) {
            paymentApiResponse.setErrorMessage("Relation with remitter can not be null or empty");
        }
        paymentApiResponse.setApiStatus(Constants.API_STATUS_INVALID);
        return paymentApiResponse;
    }

    public static boolean isValidPaymentRequest(PaymentApiRequest paymentApiRequest) {
        if (Objects.nonNull(paymentApiRequest)
                && Objects.nonNull(paymentApiRequest.getExchCode()) && !paymentApiRequest.getExchCode().isEmpty()
                && Objects.nonNull(paymentApiRequest.getPinno()) && !paymentApiRequest.getPinno().isEmpty()
                && Objects.nonNull(paymentApiRequest.getBrUserId()) && !paymentApiRequest.getBrUserId().isEmpty()
                && Objects.nonNull(paymentApiRequest.getBrCode()) && !paymentApiRequest.getBrCode().isEmpty()
                && Objects.nonNull(paymentApiRequest.getBeneIDNumber()) && !paymentApiRequest.getBeneIDNumber().isEmpty()
                && Objects.nonNull(paymentApiRequest.getDob()) && !paymentApiRequest.getDob().isEmpty()
                && Objects.nonNull(paymentApiRequest.getTranNo()) && !paymentApiRequest.getTranNo().isEmpty()
                && Objects.nonNull(paymentApiRequest.getAddress()) && !paymentApiRequest.getAddress().isEmpty()
                && Objects.nonNull(paymentApiRequest.getCity()) && !paymentApiRequest.getCity().isEmpty()
                //&& Objects.nonNull(paymentApiRequest.getZipCode()) && !paymentApiRequest.getZipCode().isEmpty()
                && Objects.nonNull(paymentApiRequest.getMobileNo()) && !paymentApiRequest.getMobileNo().isEmpty()
                && Objects.nonNull(paymentApiRequest.getPurposeOfTran()) && !paymentApiRequest.getPurposeOfTran().isEmpty()
                && Objects.nonNull(paymentApiRequest.getRelationWithRemitter()) && !paymentApiRequest.getRelationWithRemitter().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static PaymentApiResponse unauthorized(PaymentApiResponse paymentApiResponse){
        paymentApiResponse.setPayoutStatus(null);
        paymentApiResponse.setErrorMessage("Unauthorized access");
        paymentApiResponse.setApiStatus(Constants.API_STATUS_INVALID);
        return paymentApiResponse;
    }

}
