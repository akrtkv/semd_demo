package su.medsoft.rir.recipe.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static su.medsoft.rir.recipe.utils.Constants.LOCAL_DATE_FORMAT;

public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        this(null);
    }

    public LocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (value == null)
            jsonGenerator.writeNull();
        else {
            jsonGenerator.writeString(value.format(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT, Locale.ENGLISH)));
        }
    }
}
