package ru.otus.hw;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartment {
  @Data
  class ATMEntry {
    final ATM atm;
    final ATMSnapshot initialSnapshot;
  }

  List<ATMEntry> atmEntries = new ArrayList<>();

  public void addATM(ATM atm) {
    atmEntries.add(new ATMEntry(atm, atm.createSnapshot()));
  }

  public int getTotalValue() {
    return atmEntries.stream().mapToInt(atmEntry -> atmEntry.getAtm().getTotalValue()).sum();
  }

  public void restore() {
    atmEntries.forEach(atmEntry -> atmEntry.getAtm().restore(atmEntry.getInitialSnapshot()));
  }
}
