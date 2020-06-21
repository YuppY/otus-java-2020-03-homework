package ru.otus.hw.json;

import lombok.SneakyThrows;

import javax.json.Json;
import javax.json.JsonValue;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MyJSON {

  private MyJSON() {
    super();
  }

  @SneakyThrows
  private static JsonValue toJsonArrayValue(Collection<?> obj) {
    var builder = Json.createArrayBuilder();
    for (var item : obj) {
      builder = builder.add(toJsonValue(item));
    }
    return builder.build();
  }

  @SneakyThrows
  private static JsonValue toJsonObjectValue(Object obj) {
    var builder = Json.createObjectBuilder();
    for (var field : obj.getClass().getDeclaredFields()) {
      if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
        continue;
      }

      field.setAccessible(true);
      var value = field.get(obj);
      if (value != null) { // GSON ignores null values, so do we
        builder = builder.add(field.getName(), toJsonValue(value));
      }
    }
    return builder.build();
  }

  private static JsonValue toJsonValue(Object obj) {
    if (obj == null) {
      return JsonValue.NULL;
    }

    if (obj instanceof Boolean) {
      return (boolean) obj ? JsonValue.TRUE : JsonValue.FALSE;
    }

    if (obj instanceof String) {
      return Json.createValue((String) obj);
    }

    if (obj instanceof Character) {
      return Json.createValue(String.valueOf((char) obj));
    }

    if (obj instanceof Float || obj instanceof Double) {
      return Json.createValue(((Number) obj).doubleValue());
    }

    if (obj instanceof Number) {
      return Json.createValue(((Number) obj).longValue());
    }

    if (obj instanceof Object[]) {
      return toJsonArrayValue(Arrays.asList((Object[]) obj));
    }

    if (obj instanceof Collection) {
      return toJsonArrayValue((Collection<?>) obj);
    }

    return toJsonObjectValue(obj);
  }

  @SneakyThrows
  public static String toJSON(Object obj) {
    return toJsonValue(obj).toString();
  }

  @SneakyThrows
  public static String hackishToJSON(Object obj) {
    var jsonString = Json.createArrayBuilder(Collections.singletonList(obj)).build().toString();
    return jsonString.substring(1, jsonString.length() - 1);
  }
}
