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
