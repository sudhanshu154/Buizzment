package com.buizzment.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import java.time.YearMonth;

//@Component
//public class StringToYearMonthConverter implements Converter<String, YearMonth> {
//    @Override
//    public YearMonth convert(String source) {
//        return YearMonth.parse(source);
//    }
//}

@ReadingConverter
public class StringToYearMonthConverter implements Converter<String, YearMonth> {
    @Override
    public YearMonth convert(String source) {
        return YearMonth.parse(source);
    }
}

