package ru.otus.hw;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Note {
  N10(10),
  N50(50),
  N100(100),
  N500(500),
  N1000(1000),
  N5000(5000);

  @Getter
  private final int value;
}
