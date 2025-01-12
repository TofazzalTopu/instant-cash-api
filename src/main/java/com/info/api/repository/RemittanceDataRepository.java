package com.info.api.repository;

import com.info.api.entity.RemittanceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RemittanceDataRepository extends JpaRepository<RemittanceData, Long> {

    @Lock(LockModeType.READ)
    Optional<RemittanceData> findByExchangeCodeAndReferenceNo(String exchangeCode, String referenceNo);

    @Query(value = "SELECT DISTINCT referenceNo FROM RemittanceData WHERE exchangeCode = ?1 and referenceDate =?2 and referenceNo IN (?3)")
    List<String> findAllByExchangeCodeAndReferenceDateAndReferenceNumbers(String exchangeCode, Date referenceDate, List<String> referenceNumbers);

    List<RemittanceData> findAllByExchangeCodeAndReferenceNo(String exchangeCode, String referenceNo);

    List<RemittanceData> findAllByExchangeCodeAndMiddlewarePushAndSourceTypeOrProcessStatusIn(String exchangeCode, Integer middlewarePush, String sourceType, List<String> processStatuses);

    @Query(value = "SELECT DISTINCT referenceNo FROM RemittanceData WHERE exchangeCode = ?1 and referenceNo IN (?2)")
    List<String> findAllByExchangeCodeAndReferenceNumbers(String exchangeCode, List<String> referenceNumbers);

    List<RemittanceData> findAllByExchangeCodeAndSourceTypeAndProcessStatus(String exchangeCode, String sourceType, String status);

    @Modifying
    @Query("update RemittanceData r set r.processStatus = ?1 where r.referenceNo = ?2")
    int updateRemittanceStatusByReferenceNo(String status, String referenceNo);


}
