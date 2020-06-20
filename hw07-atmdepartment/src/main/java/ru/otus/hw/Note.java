package ru.otus.hw;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Note {
  NOTE_10(10),
  NOTE_50(50),
  NOTE_100(100),
  NOTE_500(500),
  NOTE_1000(1000),
  NOTE_5000(5000);

  @Getter
  private final int value;
}
