package com.buizzment.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.YearMonth;

@WritingConverter
public class YearMonthToStringConverter implements Converter<YearMonth, String> {
    @Override
    public String convert(YearMonth source) {
        return source.toString();
    }
}
