package com.info.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@Table(name = "REMITTANCE_DATA")
public class RemittanceData extends BaseRemittanceData {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REMITTANCE_DATA_GEN")
	@SequenceGenerator(name = "REMITTANCE_DATA_GEN_TEST", sequenceName = "REMITTANCE_DATA_SEQ", allocationSize = 25)
	private Long id;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RemittanceData that = (RemittanceData) o;
		return Objects.equals(exchangeCode, that.exchangeCode) && Objects.equals(referenceNo, that.referenceNo) && Objects.equals(referenceDate, that.referenceDate);
	}

	public RemittanceData(String exchangeCode, String referenceNo, Date referenceDate) {
		this.exchangeCode = exchangeCode;
		this.referenceNo = referenceNo;
		this.referenceDate = referenceDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(exchangeCode, referenceNo, referenceDate);
	}


}