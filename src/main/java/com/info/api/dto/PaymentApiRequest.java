package com.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PaymentApiRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6326694665210595043L;

	@JsonProperty("ExchCode")
	private String exchCode;

	@JsonProperty("Pinno")
	private String pinno;

	@JsonProperty("BrUserId")
	private String brUserId;

	@JsonProperty("BrCode")
	private String brCode;

	@JsonProperty("NID")
	private String beneIDNumber;

	@JsonProperty("DOB")
	private String dob;

	@JsonProperty("TranNo")
	private String tranNo;

	@JsonProperty("Address")
	private String address;

	@JsonProperty("City")
	private String city;

	@JsonProperty("ZipCode")
	private String zipCode;

	@JsonProperty("MobileNo")
	private String mobileNo;

	@JsonProperty("PurposeOfTran")
	private String purposeOfTran;

	@JsonProperty("RelationWithRemitter")
	private String relationWithRemitter;

	@JsonProperty("ipAddress")
	private String ipAddress;

	@JsonProperty("BeneIDType")
	private String beneIDType;

	@JsonProperty("BeneIDIssuedBy")
	private String beneIDIssuedBy;

	@JsonProperty("BeneIDIssuedByCountry")
	private String beneIDIssuedByCountry;

	@JsonProperty("BeneIDIssuedByState")
	private String beneIDIssuedByState;

	@JsonProperty("BeneIDIssueDate")
	private String beneIDIssueDate;

	@JsonProperty("BeneIDExpirationDate")
	private String beneIDExpirationDate;

	@JsonProperty("BeneOccupation")
	private String beneOccupation;

	@JsonProperty("BeneGender")
	private String beneGender;

	@JsonProperty("BeneTaxID")
	private String beneTaxID;

	@JsonProperty("BeneCustRelationship")
	private String beneCustRelationship;

	public String getExchCode() {
		return exchCode;
	}

	public void setExchCode(String exchCode) {
		this.exchCode = exchCode;
	}

	public String getPinno() {
		return pinno;
	}

	public void setPinno(String pinno) {
		this.pinno = pinno;
	}

	public String getBrUserId() {
		return brUserId;
	}

	public void setBrUserId(String brUserId) {
		this.brUserId = brUserId;
	}

	public String getBrCode() {
		return brCode;
	}

	public void setBrCode(String brCode) {
		this.brCode = brCode;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getTranNo() {
		return tranNo;
	}

	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPurposeOfTran() {
		return purposeOfTran;
	}

	public void setPurposeOfTran(String purposeOfTran) {
		this.purposeOfTran = purposeOfTran;
	}

	public String getRelationWithRemitter() {
		return relationWithRemitter;
	}

	public void setRelationWithRemitter(String relationWithRemitter) {
		this.relationWithRemitter = relationWithRemitter;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getBeneIDType() {
		return beneIDType;
	}

	public void setBeneIDType(String beneIDType) {
		this.beneIDType = beneIDType;
	}

	public String getBeneIDIssuedBy() {
		return beneIDIssuedBy;
	}

	public void setBeneIDIssuedBy(String beneIDIssuedBy) {
		this.beneIDIssuedBy = beneIDIssuedBy;
	}

	public String getBeneIDIssuedByCountry() {
		return beneIDIssuedByCountry;
	}

	public void setBeneIDIssuedByCountry(String beneIDIssuedByCountry) {
		this.beneIDIssuedByCountry = beneIDIssuedByCountry;
	}

	public String getBeneIDIssuedByState() {
		return beneIDIssuedByState;
	}

	public void setBeneIDIssuedByState(String beneIDIssuedByState) {
		this.beneIDIssuedByState = beneIDIssuedByState;
	}

	public String getBeneIDIssueDate() {
		return beneIDIssueDate;
	}

	public void setBeneIDIssueDate(String beneIDIssueDate) {
		this.beneIDIssueDate = beneIDIssueDate;
	}

	public String getBeneIDExpirationDate() {
		return beneIDExpirationDate;
	}

	public void setBeneIDExpirationDate(String beneIDExpirationDate) {
		this.beneIDExpirationDate = beneIDExpirationDate;
	}

	public String getBeneOccupation() {
		return beneOccupation;
	}

	public void setBeneOccupation(String beneOccupation) {
		this.beneOccupation = beneOccupation;
	}

	public String getBeneGender() {
		return beneGender;
	}

	public void setBeneGender(String beneGender) {
		this.beneGender = beneGender;
	}

	public String getBeneTaxID() {
		return beneTaxID;
	}

	public void setBeneTaxID(String beneTaxID) {
		this.beneTaxID = beneTaxID;
	}

	public String getBeneCustRelationship() {
		return beneCustRelationship;
	}

	public void setBeneCustRelationship(String beneCustRelationship) {
		this.beneCustRelationship = beneCustRelationship;
	}

	public String getBeneIDNumber() {
		return beneIDNumber;
	}

	public void setBeneIDNumber(String beneIDNumber) {
		this.beneIDNumber = beneIDNumber;
	}

}
