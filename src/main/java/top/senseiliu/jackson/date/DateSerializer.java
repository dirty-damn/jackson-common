package top.senseiliu.jackson.date;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            String s = Format.SDF_1.format(value);
            gen.writeString(s);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("[Jackson]序列化Date时发生异常，msg:" + e.getMessage());
        }
    }
}
