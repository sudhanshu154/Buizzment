package com.buizzment.config;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class YearMonthCodec implements Codec<YearMonth> {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    
    @Override
    public YearMonth decode(BsonReader reader, DecoderContext decoderContext) {
        String value = reader.readString();
        return YearMonth.parse(value, FORMATTER);
    }
    
    @Override
    public void encode(BsonWriter writer, YearMonth value, EncoderContext encoderContext) {
        writer.writeString(value.format(FORMATTER));
    }
    
    @Override
    public Class<YearMonth> getEncoderClass() {
        return YearMonth.class;
    }
} 