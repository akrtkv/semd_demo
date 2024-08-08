package com.github.akrtkv.semd_demo.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.github.akrtkv.semd_demo.util.Constants.ZONED_DATE_TIME_FORMAT;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    public ZonedDateTimeSerializer() {
        this(null);
    }

    public ZonedDateTimeSerializer(Class<ZonedDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (value == null)
            jsonGenerator.writeNull();
        else {
            jsonGenerator.writeString(value.format(DateTimeFormatter.ofPattern(ZONED_DATE_TIME_FORMAT)));
        }
    }
}
