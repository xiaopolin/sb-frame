package com.xpl.framework.config;

import ch.qos.logback.classic.pattern.MDCConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.Map;

public class MDCConfig extends MDCConverter {

    @Override
    public String convert(ILoggingEvent event) {
        Map<String, String> mdc = event.getMDCPropertyMap();
        mdc.put("LOG_ID", "系统异常：");
        String msg = super.convert(event);
        mdc.clear();
        return msg;
    }
}
