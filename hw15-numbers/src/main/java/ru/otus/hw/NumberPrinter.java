package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.Phaser;

@RequiredArgsConstructor
public class NumberPrinter {
  private final boolean firstPrinter;

  private static final int MIN_NUMBER = 1;
  private static final int MAX_NUMBER = 10;

  private static final Random RAND = new Random();

  private static final Phaser PHASER = new Phaser(1);

  @SneakyThrows
  void run() {
    if (!firstPrinter) {
      // ждём завершения начальной фазы первого потока
      PHASER.awaitAdvance(0);
    }

    var thread = Thread.currentThread();
    var number = MIN_NUMBER;
    var shift = 1;

    while (!thread.isInterrupted()) {
      //      Thread.sleep(RAND.nextInt(1000));
      System.out.println((firstPrinter ? "" : " ") + number);
      number += shift;
      if (number == MAX_NUMBER || number == MIN_NUMBER) {
        shift = -shift;
      }
      // ждём завершения фазы, в которой работает следующий поток
      PHASER.awaitAdvance(PHASER.arrive() + 1);
    }
  }
}
