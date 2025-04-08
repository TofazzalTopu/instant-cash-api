package com.info.api.util;

import com.info.api.constants.Constants;
import com.info.api.dto.PaymentApiRequest;
import com.info.api.dto.PaymentApiResponse;
import com.info.api.dto.ic.LoginErrorResponse;
import com.info.api.dto.ic.TransactionReportRequestBody;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ParseUtil {

    private ParseUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ParseUtil.class);

    public static PaymentApiRequest parseAndPrepareRequest(String data, String requestIp) {
        PaymentApiRequest paymentApiRequest = new PaymentApiRequest();
        try {
            JSONObject json = new JSONObject(data);

            paymentApiRequest.setIpAddress(requestIp);

            if (!json.isNull("Address"))
                paymentApiRequest.setAddress(json.getString("Address"));

            if (!json.isNull("BrCode"))
                paymentApiRequest.setBrCode(json.getString("BrCode"));

            if (!json.isNull("BrUserId"))
                paymentApiRequest.setBrUserId(json.getString("BrUserId"));

            if (!json.isNull("City"))
                paymentApiRequest.setCity(json.getString("City"));

            if (!json.isNull("DOB"))
                paymentApiRequest.setDob(json.getString("DOB"));

            if (!json.isNull("ExchCode"))
                paymentApiRequest.setExchCode(json.getString("ExchCode"));

            if (!json.isNull("MobileNo"))
                paymentApiRequest.setMobileNo(json.getString("MobileNo"));

            if (!json.isNull("BeneIDNumber"))
                paymentApiRequest.setBeneIDNumber(json.getString("BeneIDNumber"));

            if (!json.isNull("Pinno"))
                paymentApiRequest.setPinno(json.getString("Pinno"));

            if (!json.isNull("PurposeOfTran"))
                paymentApiRequest.setPurposeOfTran(json.getString("PurposeOfTran"));

            if (!json.isNull("RelationWithRemitter"))
                paymentApiRequest.setRelationWithRemitter(json.getString("RelationWithRemitter"));

            if (!json.isNull("TranNo"))
                paymentApiRequest.setTranNo(json.getString("TranNo"));

            if (!json.isNull("ZipCode"))
                paymentApiRequest.setZipCode(json.getString("ZipCode"));

            if (!json.isNull("BeneIDType"))
                paymentApiRequest.setBeneIDType(json.getString("BeneIDType"));

            if (!json.isNull("BeneIDIssuedBy"))
                paymentApiRequest.setBeneIDIssuedBy(json.getString("BeneIDIssuedBy"));

            if (!json.isNull("BeneIDIssuedByCountry"))
                paymentApiRequest.setBeneIDIssuedByCountry(json.getString("BeneIDIssuedByCountry"));

            if (!json.isNull("BeneIDIssuedByState"))
                paymentApiRequest.setBeneIDIssuedByState(json.getString("BeneIDIssuedByState"));

            if (!json.isNull("BeneIDIssueDate"))
                paymentApiRequest.setBeneIDIssueDate(json.getString("BeneIDIssueDate"));

            if (!json.isNull("BeneIDExpirationDate"))
                paymentApiRequest.setBeneIDExpirationDate(json.getString("BeneIDExpirationDate"));

            if (!json.isNull("BeneOccupation"))
                paymentApiRequest.setBeneOccupation(json.getString("BeneOccupation"));

            if (!json.isNull("BeneGender"))
                paymentApiRequest.setBeneGender(json.getString("BeneGender"));

            if (!json.isNull("BeneTaxID"))
                paymentApiRequest.setBeneTaxID(json.getString("BeneTaxID"));

            if (!json.isNull("BeneCustRelationship"))
                paymentApiRequest.setBeneCustRelationship(json.getString("BeneCustRelationship"));

        } catch (Exception e) {
            logger.error("Error in parseAndPrepareRequest: Error = {} ", e.getMessage());
            paymentApiRequest = new PaymentApiRequest();
        }
        return paymentApiRequest;
    }

    public static PaymentApiResponse mapPaymentApiResponse(PaymentApiResponse paymentApiResponse, PaymentApiRequest paymentApiRequest) {
        paymentApiResponse.setPayoutStatus(null);

        if (Objects.isNull(paymentApiRequest)) {
            paymentApiResponse.setErrorMessage("Request Can not be empty");
        } else if (isNullOrEmpty(paymentApiRequest.getExchCode())) {
            paymentApiResponse.setErrorMessage("Exchange Code can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getPinno())) {
            paymentApiResponse.setErrorMessage("Pin No can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getBrUserId())) {
            paymentApiResponse.setErrorMessage("Branch User ID can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getBrCode())) {
            paymentApiResponse.setErrorMessage("Branch Code can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getBeneIDNumber())) {
            paymentApiResponse.setErrorMessage("Beneficiary ID can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getDob())) {
            paymentApiResponse.setErrorMessage("Date of Birth can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getTranNo())) {
            paymentApiResponse.setErrorMessage("Tran No can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getAddress())) {
            paymentApiResponse.setErrorMessage("Address can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getCity())) {
            paymentApiResponse.setErrorMessage("City can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getMobileNo())) {
            paymentApiResponse.setErrorMessage("Mobile No can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getPurposeOfTran())) {
            paymentApiResponse.setErrorMessage("Purpose of Transaction can not be null or empty");
        } else if (isNullOrEmpty(paymentApiRequest.getRelationWithRemitter())) {
            paymentApiResponse.setErrorMessage("Relation with remitter can not be null or empty");
        }
        paymentApiResponse.setApiStatus(Constants.API_STATUS_INVALID);
        return paymentApiResponse;
    }

    public static boolean isValidPaymentRequest(PaymentApiRequest paymentApiRequest) {
        return Objects.nonNull(paymentApiRequest)
                && isNotNullAndNotEmpty(paymentApiRequest.getExchCode())
                && isNotNullAndNotEmpty(paymentApiRequest.getPinno())
                && isNotNullAndNotEmpty(paymentApiRequest.getBrUserId())
                && isNotNullAndNotEmpty(paymentApiRequest.getBrCode())
                && isNotNullAndNotEmpty(paymentApiRequest.getBeneIDNumber())
                && isNotNullAndNotEmpty(paymentApiRequest.getDob())
                && isNotNullAndNotEmpty(paymentApiRequest.getTranNo())
                && isNotNullAndNotEmpty(paymentApiRequest.getAddress())
                && isNotNullAndNotEmpty(paymentApiRequest.getCity())
                //&& isNotNullAndNotEmpty(paymentApiRequest.getZipCode())
                && isNotNullAndNotEmpty(paymentApiRequest.getMobileNo())
                && isNotNullAndNotEmpty(paymentApiRequest.getPurposeOfTran())
                && isNotNullAndNotEmpty(paymentApiRequest.getRelationWithRemitter());
    }

    public static boolean isValidICTransactionReportBody(TransactionReportRequestBody report) {
        return isNotNullAndNotEmpty(report.getUserId()) && isNotNullAndNotEmpty(report.getPassword()) &&
                isNotNullAndNotEmpty(report.getExchcode()) && isNotNullAndNotEmpty(report.getFromDate()) && isNotNullAndNotEmpty(report.getToDate());
    }


    public static boolean isNotNullAndNotEmpty(String value) {
        return Objects.nonNull(value) && !"".equals(value) && !value.isEmpty();
    }

    public static boolean isNotNullAndNotEmpty(Object value) {
        return Objects.nonNull(value) && !"".equals(value) && !value.toString().isEmpty();
    }

    public static boolean isNotNullAndNotEmptyList(List<? extends Object> list) {
        return Objects.nonNull(list) && !list.isEmpty();
    }

    public static boolean isNullOrEmpty(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    public static boolean isNullOrEmpty(String... values) {
        return Arrays.stream(values).anyMatch(v -> v == null || v.isEmpty());
    }

    public static PaymentApiResponse unAuthorized(PaymentApiResponse paymentApiResponse) {
        paymentApiResponse.setPayoutStatus(null);
        paymentApiResponse.setErrorMessage(Constants.UNAUTHORIZED_ACCESS);
        paymentApiResponse.setApiStatus(Constants.API_STATUS_INVALID);
        return paymentApiResponse;
    }

    public static LoginErrorResponse unAuthorized() {
        return LoginErrorResponse.builder().payoutStatus(null).apiStatus(Constants.API_STATUS_INVALID).errorMessage(Constants.UNAUTHORIZED_ACCESS).build();
    }

}
