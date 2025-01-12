package com.info.api.service.common;

import com.info.api.entity.ExchangeHouseProperty;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class LoadExchangeHouseProperty {

    private final ExchangeHousePropertyService exchangeHousePropertyService;

    @Getter
    public static final List<ExchangeHouseProperty> exchangeHousePropertyList = new ArrayList<>();

    public LoadExchangeHouseProperty(ExchangeHousePropertyService exchangeHousePropertyService) {
        this.exchangeHousePropertyService = exchangeHousePropertyService;
    }

    @PostConstruct
    protected void exchangeHouseProperty() {
        exchangeHousePropertyList.addAll(exchangeHousePropertyService.findAll());
    }

    public static List<ExchangeHouseProperty> getICExchangeHouseProperty() {
        Predicate<ExchangeHouseProperty> icPredicate = e -> e.getKeyLabel().startsWith("IC_");
        return exchangeHousePropertyList.stream().filter(icPredicate).collect(Collectors.toList());
    }


}
