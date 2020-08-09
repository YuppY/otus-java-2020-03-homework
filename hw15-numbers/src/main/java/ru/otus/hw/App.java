package ru.otus.hw;

public class App {
  public static void main(String[] args) {
    new Thread(new NumberPrinter(true)::run).start();
    new Thread(new NumberPrinter(false)::run).start();
  }
}
