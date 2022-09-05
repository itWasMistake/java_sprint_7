package main.java.adapters;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatWriter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");
    private static final DateTimeFormatter formatReader = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.format(formatWriter));

    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatReader);
    }

}
