package com.info.api.schedular;

import com.info.api.processor.InstantCashAPIProcessor;
import com.info.api.util.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class ApiClientScheduler {

    public static final Logger logger = LoggerFactory.getLogger(ApiClientScheduler.class);

    private final InstantCashAPIProcessor instantCashAPIProcessor;

    public ApiClientScheduler(InstantCashAPIProcessor instantCashAPIProcessor) {
        this.instantCashAPIProcessor = instantCashAPIProcessor;
    }


    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void apiClientScheduler() {
        logger.info("ICScheduler started.");
        ExecutorService instantCashExecutor = Executors.newFixedThreadPool(1);
        ExecutorService riaExecutor = Executors.newFixedThreadPool(1);

        try {
            logger.info("InstantCashAPIProcessor started");
            instantCashExecutor.execute(() -> instantCashAPIProcessor.process(ApiUtil.getICExchangeProperties()));
        } catch (Exception ex) {
            logger.error("Error in ApiClientScheduler: {}", ex.getMessage());
        } finally {
            riaExecutor.shutdown();
            instantCashExecutor.shutdown();
        }
        logger.error("ICScheduler ended.");
    }

}
