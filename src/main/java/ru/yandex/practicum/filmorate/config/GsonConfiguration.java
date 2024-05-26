package ru.yandex.practicum.filmorate.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import ru.yandex.practicum.filmorate.serialization.DurationDeserializer;
import ru.yandex.practicum.filmorate.serialization.DurationSerializer;
import ru.yandex.practicum.filmorate.serialization.InstantDeserializer;
import ru.yandex.practicum.filmorate.serialization.InstantSerializer;

import java.time.Duration;
import java.time.Instant;

@Configuration
public class GsonConfiguration {

    @Bean
    public InstantDeserializer instantDeserializer() {
        return new InstantDeserializer();
    }

    @Bean
    public InstantSerializer instantSerializer() {
        return new InstantSerializer();
    }

    @Bean
    public DurationSerializer durationSerializer() {
        return new DurationSerializer();
    }

    @Bean
    public DurationDeserializer durationDeserializer() {
        return new DurationDeserializer();
    }

    @Bean
    public HttpMessageConverters customConverters() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantDeserializer())
                .registerTypeAdapter(Instant.class, new InstantSerializer())
                .registerTypeAdapter(Duration.class, new DurationSerializer())
                .registerTypeAdapter(Duration.class, new DurationDeserializer())
                .create();

        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson);

        return new HttpMessageConverters(gsonConverter);
    }
//    @Bean
//    public Gson gson(InstantDeserializer instantDeserializer, InstantSerializer instantSerializer,
//                     DurationSerializer durationSerializer, DurationDeserializer durationDeserializer) {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Instant.class, instantDeserializer);
//        gsonBuilder.registerTypeAdapter(Instant.class, instantSerializer);
//        gsonBuilder.registerTypeAdapter(Duration.class, durationSerializer);
//        gsonBuilder.registerTypeAdapter(Duration.class, durationDeserializer);
//        return gsonBuilder.create();
//    }
}
