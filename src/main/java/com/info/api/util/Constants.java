package com.info.api.util;

public class Constants {
	public static final String API_STATUS_VALID = "VALID";
	public static final String API_STATUS_INVALID = "INVALID";
	public static final String API_STATUS_ERROR = "ERROR";
	public static final String CANCEL_SETTLED = "SETTLED";
	public static final String CANCEL_UNSETTLED = "UNSETTLED";
	public static final int TRY_COUNT = 5;

	public static final int MIDDLEWARE_PUSH_UNDONE = 0;
	public static final int MIDDLEWARE_PUSH_DONE = 1;
	public static final int MIDDLEWARE_PUSH_ERROR = 2;

	public static final String API_SOURCE_TYPE = "API_PULL";

	public static final String REFERENCE_NO_ALREADY_EXIST = "Reference no already exist!";
	public static final String REFERENCE_NOT_EXIST = "Reference not exist!";


	public static final String REQUEST_TYPE_SEARCH = "SEARCH";
	public static final String REQUEST_TYPE_PAYMENT = "PAYMENT";
	public static final String REQUEST_TYPE_DOWNLOAD_REQ = "DOWNLOAD_REQ";
	public static final String REQUEST_TYPE_NOTIFY_REM_STATUS = "NOTIFY_REM_STATUS";

	public static final String REQUEST_TYPE_IC_FETCH_OUTSTANDING_TRANSACTION = "IC_FETCH_OUTSTANDING_TRANSACTION";
	public static final String REQUEST_TYPE_IC_UNLOCK_TRANSACTION = "IC_UNLOCK_TRANSACTION";
	public static final String REQUEST_TYPE_IC_CONFIRM_TRANSACTION = "IC_CONFIRM_TRANSACTION";
	public static final String REQUEST_TYPE_IC_RETRIEVE_PAYMENT_STATUS_TRANSACTION = "IC_RETRIEVE_PAYMENT_STATUS_TRANSACTION";
	public static final String REQUEST_TYPE_IC_RECEIVE_PAYMENT_TRANSACTION = "IC_RECEIVE_PAYMENT_TRANSACTION";

	public static final String EXCHANGE_HOUSE_INSTANT_CASH = "INSTANT_CASH";


    public static final String API_TRACE_CREATION_ERROR = "ApiTrace creation error.";
    public static final String TRACING_REMOVED_BECAUSE_NO_RECORD_FOUND_TRACE_ID ="{}, Tracing removed because no record found, TraceID: {} ";
    public static final String EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_OUTSTANDING_REMITTANCE = "ExchangeHouseProperty not exist for ICOutstandingRemittance!";
    public static final String EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_NOTIFY_STATUS = "ExchangeHouseProperty not exist for notifyPaymentStatus!";
    public static final String EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_UNLOCK_REMITTANCE = "ExchangeHouseProperty not exist for unlockICOutstandingRemittance!";
    public static final String EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_RECEIVE_PAYMENT = "ExchangeHouseProperty not exist for receivePayment!";
    public static final String EXCHANGE_HOUSE_PROPERTY_NOT_EXIST_FOR_RETRIEVE_PAYMENT_STATUS = "ExchangeHouseProperty not exist for retrievePaymentStatus!";

	public static final String PAYMENT_RECEIVE_SUCCESSFULLY = "Instant Cash Payment Receive Successfully.";
	public static final String STATUS_RETRIEVE_SUCCESSFULLY = "Instant Cash Payment Status Retrieve Successfully.";
	public static final String PAYMENT_UNLOCK_SUCCESSFULLY = "Instant Cash Payment Unlock Successfully.";



}
