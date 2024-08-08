package com.github.akrtkv.semd_demo.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.github.akrtkv.semd_demo.util.Constants.LOCAL_DATE_TIME_FORMAT;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer() {
        this(null);
    }

    public LocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (value == null)
            jsonGenerator.writeNull();
        else {
            jsonGenerator.writeString(value.format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT, Locale.ENGLISH)));
        }
    }
}
