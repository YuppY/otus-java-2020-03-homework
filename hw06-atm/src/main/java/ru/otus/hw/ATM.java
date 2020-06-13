package ru.otus.hw;

import lombok.SneakyThrows;

import java.util.stream.Collectors;

public class ATM {
  Notes cartridges;

  public ATM() {
    cartridges = new Notes();
  }

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
    var sortedNotes =
        cartridges.keySet().stream()
            .sorted((a, b) -> (b.getValue() - a.getValue()))
            .collect(Collectors.toList());

    for (var note : sortedNotes) {
      var requiredValue = value / note.getValue();
      if (requiredValue == 0) {
        break;
      }

      var count = Math.min(cartridges.get(note), requiredValue);
      value = value - (count * note.getValue());
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
}
