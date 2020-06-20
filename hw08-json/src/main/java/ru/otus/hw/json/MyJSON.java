package ru.otus.hw.json;

import lombok.SneakyThrows;
import ru.otus.hw.json.exceptions.UnknownObjectType;

import javax.json.Json;
import javax.json.JsonValue;
import java.util.Collection;
import java.util.Collections;

public class MyJSON {
  private static JsonValue toJsonValue(Object obj) throws UnknownObjectType {
    if (obj == null) {
      return JsonValue.NULL;
    }

    if (obj instanceof Boolean) {
      return (boolean) obj ? JsonValue.TRUE : JsonValue.FALSE;
    }

    if (obj instanceof String) {
      return Json.createValue((String) obj);
    }

    if (obj instanceof Integer) {
      return Json.createValue((int) obj);
    }

    if (obj instanceof Long) {
      return Json.createValue((long) obj);
    }

    if (obj instanceof Double) {
      return Json.createValue((double) obj);
    }

    if (obj instanceof Collection) {
      return Json.createArrayBuilder((Collection<?>) obj).build();
    }

    throw new UnknownObjectType(obj.getClass().getName());
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
