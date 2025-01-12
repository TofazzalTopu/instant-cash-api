package com.info.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Slf4j
public class ObjectConverter {

    public static final Logger logger = LoggerFactory.getLogger(ObjectConverter.class);

    public static <T> String convertObjectToString(T data) {
        try {
            return Objects.nonNull(data) ? new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(data) : "";
        } catch (Exception e) {
            logger.info("Data parse error -----------------------------> \r\n" + data);
        }
        return "";
    }
}
