package com.example.legacy.support;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeadLetterQueue {

    private static final List<DeadLetterItem> items = new ArrayList<>();
    private static final String FILE_PATH = "dead-letter-queue-legacy.json";

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static final Object lock = new Object();

    public static void add(DeadLetterItem item) {
        synchronized (lock) {
            items.add(item);
            try {
                mapper.writeValue(new File(FILE_PATH), items);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<DeadLetterItem> getAll() {
        synchronized (lock) {
            return new ArrayList<>(items);
        }
    }
}
