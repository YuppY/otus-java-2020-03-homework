package ru.otus.hw;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.TreeMap;

public class ATM {
  TreeMap<Note, Integer> cartridges =
      new TreeMap<>(Comparator.comparingInt(Note::getValue).reversed());

  public void addNotes(Note note, int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("positive count required");
    }

    cartridges.put(note, cartridges.getOrDefault(note, 0) + count);
  }

  public static class NotEnoughNotes extends Exception {}

  @SneakyThrows
  public Notes getNotes(int value) {
    if (value <= 0) {
      throw new IllegalArgumentException("positive value required");
    }

    var result = new Notes();
    for (var entry : cartridges.entrySet()) {
      var note = entry.getKey();
      var noteValue = note.getValue();
      var cartridgeCount = entry.getValue();
      var requiredCount = value / noteValue;
      if (requiredCount == 0) {
        break;
      }

      var count = Math.min(cartridgeCount, requiredCount);
      value = value - (count * noteValue);
      result.put(note, count);
    }

    if (value != 0) {
      throw new NotEnoughNotes();
    }

    result.forEach(
        (note, count) -> {
          var atmCount = cartridges.get(note);
          if (count.equals(atmCount)) {
            cartridges.remove(note);
          } else {
            cartridges.put(note, atmCount - count);
          }
        });

    return result;
  }

  public int getTotalValue() {
    return cartridges.entrySet().stream()
        .mapToInt(
            entry -> {
              var note = entry.getKey();
              var count = entry.getValue();
              return count * note.getValue();
            })
        .sum();
  }

  public Notes createSnapshot() {
    var result = new Notes();
    cartridges.forEach(result::put);
    return result;
  }

  public void restore(@NotNull Notes snapshot) {
    cartridges.clear();
    snapshot.forEach(cartridges::put);
  }
}
