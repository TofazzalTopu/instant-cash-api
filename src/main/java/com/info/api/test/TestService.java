package com.info.api.test;

import com.info.api.entity.RemittanceData;
import com.info.api.repository.RemittanceDataRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Predicate;

@Service
public class TestService {

    private final RemittanceDataRepository remittanceDataRepository;
    public TestService(RemittanceDataRepository remittanceDataRepository) {
        this.remittanceDataRepository = remittanceDataRepository;
    }

    Specification<RemittanceData> ageLessThan18 = (root, query, cb) -> cb.lessThan(root.get("age").as(Integer.class), 18);
    Specification<RemittanceData> ageLessThan17 = (root, query, cb) -> cb.lessThan(root.get("age").as(Integer.class), 18);
    Predicate<RemittanceData> predicate = r -> r.getExchangeCode().equals("1452070054");
    Specification<RemittanceData> ageLessThan = ageLessThan17;


//    Specification<String> reference =  (root, query, cb) -> cb.equals("1452070054");



    public Specification<RemittanceData> isLongTermCustomer() {
        return (root, query, builder) -> {
            LocalDate date = LocalDate.now().minusDays(1);
            builder.equal(root.get("referenceNo"), "1452070054");
            builder.lessThan(root.get("processDate"), date);
            return builder.and();
        };
    }
}
