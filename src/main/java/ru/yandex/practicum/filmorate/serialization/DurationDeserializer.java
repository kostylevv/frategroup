package ru.yandex.practicum.filmorate.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException {
        int value = jsonParser.getIntValue();
        if (value < 300) {
            return Duration.ofSeconds(value);
        } else {
            return Duration.ofMinutes(value);
        }
    }
}


