package ru.otus.hw.json;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

public class TestMyJSON {
  @Test
  void testBasic() {
    Assertions.assertEquals("null", MyJSON.toJSON(null));
    Assertions.assertEquals("true", MyJSON.toJSON(true));
    Assertions.assertEquals("false", MyJSON.toJSON(false));
    Assertions.assertEquals("99", MyJSON.toJSON(99));
    Assertions.assertEquals("99", MyJSON.toJSON(99L));
    Assertions.assertEquals("3.141592654", MyJSON.toJSON(3.141592654));
  }

  @Test
  void testCollection() {
    var obj = Arrays.asList("foo", false, Arrays.asList(Set.of(1, "2"), null));
    Assertions.assertEquals(new Gson().toJson(obj), MyJSON.toJSON(obj));
  }

  private static class InnerTestClass {
    private double d1 = 53d;
    private Double d2 = 54d;
    private int[] ints = {1, 2, 3, 4};
  }

  private static class TestClass {
    private String nullStr = null;
    private String str = "aaaaaaaaaa";
    private char c1 = 'b';
    private Character c2 = 'c';
    private static final String constStr = "ddddddddddd";
    private transient String transientStr = "eeeeeeee";
    private int i1 = 53;
    private Integer i2 = 54;
    private short i3 = 55;
    private Short i4 = 56;
    private long i5 = 57L;
    private Long i6 = 58L;
    private float f1 = 59f;
    private Float f2 = 60f;
    private int[] ints = {1, 2, 3, 4};

    private InnerTestClass innerTestClass = new InnerTestClass();
    private InnerTestClass[] innerTestClasses = new InnerTestClass[] {new InnerTestClass()};
  }

  @Test
  void testObject() {
    var obj = new TestClass();
    Assertions.assertEquals(MyJSON.toJSON(obj), new Gson().toJson(obj));
  }

  @Test
  void testHackBasic() {
    Assertions.assertEquals("null", MyJSON.hackishToJSON(null));
    Assertions.assertEquals("true", MyJSON.hackishToJSON(true));
    Assertions.assertEquals("false", MyJSON.hackishToJSON(false));
    Assertions.assertEquals("99", MyJSON.hackishToJSON(99));
    Assertions.assertEquals("99", MyJSON.hackishToJSON(99L));
    Assertions.assertEquals("3.141592654", MyJSON.hackishToJSON(3.141592654));
  }

  @Test
  void testHackCollection() {
    var obj = Arrays.asList("foo", false, Arrays.asList(Set.of(1, "2"), null));
    Assertions.assertEquals(new Gson().toJson(obj), MyJSON.hackishToJSON(obj));
  }
}
