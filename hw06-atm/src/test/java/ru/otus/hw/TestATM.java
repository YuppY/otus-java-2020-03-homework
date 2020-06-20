package ru.otus.hw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.exceptions.NotEnoughNotes;

public class TestATM {
  @Test
  void testAddNotes() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          var atm = new ATM();
          atm.addNotes(Note.N10, -5);
        });

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          var atm = new ATM();
          atm.addNotes(Note.N10, 0);
        });

    var atm = new ATM();
    atm.addNotes(Note.N10, 1);
    Assertions.assertEquals(10, atm.getTotalValue());
  }

  @Test
  void testGetNotes() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          var atm = new ATM();
          atm.getNotes(-5);
        });

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          var atm = new ATM();
          atm.getNotes(0);
        });

    var atm = new ATM();
    atm.addNotes(Note.N10, 5);
    atm.addNotes(Note.N50, 1);

    Assertions.assertEquals(new Notes(Note.N10, Note.N10, Note.N50), atm.getNotes(70));
    Assertions.assertEquals(30, atm.getTotalValue());
  }

  @Test
  void testGetNotesNotEnoughNotesLesserValue() {
    var atm = new ATM();
    atm.addNotes(Note.N50, 1);

    Assertions.assertThrows(NotEnoughNotes.class, () -> atm.getNotes(100));
    Assertions.assertEquals(50, atm.getTotalValue());
  }

  @Test
  void testGetNotesNotEnoughNotesGreaterValue() {
    var atm = new ATM();
    atm.addNotes(Note.N1000, 1);

    Assertions.assertThrows(NotEnoughNotes.class, () -> atm.getNotes(100));
    Assertions.assertEquals(1000, atm.getTotalValue());
  }

  @Test
  void testGetTotalValue() {
    var atm = new ATM();
    Assertions.assertEquals(0, atm.getTotalValue());

    atm.addNotes(Note.N10, 10);
    atm.addNotes(Note.N500, 5);
    Assertions.assertEquals(2600, atm.getTotalValue());
  }
}
