package ru.yandex.practicum.filmorate.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Duration;

public class DurationSerializer extends JsonSerializer<Duration> {

    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        long seconds = duration.getSeconds();
        if (duration.toMinutes() < 5) {
            jsonGenerator.writeNumber(seconds);
        } else {
            jsonGenerator.writeNumber(duration.toMinutes());
        }
    }
}
