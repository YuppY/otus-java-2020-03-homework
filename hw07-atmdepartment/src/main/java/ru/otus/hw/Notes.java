package ru.otus.hw;

import java.util.HashMap;

public class Notes extends HashMap<Note, Integer> {
    public Notes(Note... notes) {
        for (var note : notes) {
            put(note, getOrDefault(note, 0) + 1);
        }
    }
}
