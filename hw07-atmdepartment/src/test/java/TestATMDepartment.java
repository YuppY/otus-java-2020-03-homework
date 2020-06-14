import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.ATM;
import ru.otus.hw.ATMDepartment;
import ru.otus.hw.Note;

public class TestATMDepartment {
  @Test
  void testAddATMGetTotalValue() {
    var department = new ATMDepartment();

    Assertions.assertEquals(0, department.getTotalValue());

    var atm1 = new ATM();
    atm1.addNotes(Note.NOTE_1000, 10);
    department.addATM(atm1);

    var atm2 = new ATM();
    atm1.addNotes(Note.NOTE_10, 20);
    atm1.addNotes(Note.NOTE_5000, 10);
    department.addATM(atm2);

    Assertions.assertEquals(60200, department.getTotalValue());
  }

  @Test
  void testRestore() {
    var department = new ATMDepartment();

    var atm1 = new ATM();
    atm1.addNotes(Note.NOTE_1000, 10);
    department.addATM(atm1);

    var atm2 = new ATM();
    atm2.addNotes(Note.NOTE_5000, 5);
    department.addATM(atm2);

    atm1.addNotes(Note.NOTE_500, 5);
    Assertions.assertEquals(12500, atm1.getTotalValue());
    atm2.addNotes(Note.NOTE_10, 100);
    Assertions.assertEquals(26000, atm2.getTotalValue());

    department.restore();
    Assertions.assertEquals(10000, atm1.getTotalValue());
    Assertions.assertEquals(25000, atm2.getTotalValue());
  }
}
