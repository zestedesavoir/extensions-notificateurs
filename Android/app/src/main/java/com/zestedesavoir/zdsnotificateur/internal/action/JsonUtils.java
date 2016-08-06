package com.zestedesavoir.zdsnotificateur.internal.action;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Gerard Paligot
 */
public final class JsonUtils {

  public static long getLong(JsonObject jsonObject, String field) {
    return getLong(jsonObject, field, 0);
  }

  public static long getLong(JsonObject jsonObject, String field, long defaultValue) {
    if (jsonObject.get(field) != null && jsonObject.get(field).isJsonPrimitive()) {
      return jsonObject.getAsJsonPrimitive(field).getAsLong();
    }
    return defaultValue;
  }

  public static int getInt(JsonObject jsonObject, String field) {
    return getInt(jsonObject, field, 0);
  }

  public static int getInt(JsonObject jsonObject, String field, int defaultValue) {
    if (jsonObject.get(field) != null && jsonObject.get(field).isJsonPrimitive()) {
      return jsonObject.getAsJsonPrimitive(field).getAsInt();
    }
    return defaultValue;
  }

  public static boolean getBoolean(JsonObject jsonObject, String field) {
    return getBoolean(jsonObject, field, false);
  }

  public static boolean getBoolean(JsonObject jsonObject, String field, boolean defaultValue) {
    if (jsonObject.get(field) != null && jsonObject.get(field).isJsonPrimitive()) {
      return jsonObject.getAsJsonPrimitive(field).getAsBoolean();
    }
    return defaultValue;
  }

  public static String getString(JsonObject jsonObject, String field) {
    return getString(jsonObject, field, "");
  }

  public static String getString(JsonObject jsonObject, String field, String defaultValue) {
    if (jsonObject.get(field) != null && jsonObject.get(field).isJsonPrimitive()) {
      return jsonObject.getAsJsonPrimitive(field).getAsString();
    }
    return defaultValue;
  }

  public static Date getDate(JsonObject jsonObject, String field) {
    return getDate(jsonObject, field, null);
  }

  public static Date getDate(JsonObject jsonObject, String field, Date defaultValue) {
    if (jsonObject.get(field) != null && !jsonObject.get(field).isJsonNull() && jsonObject.get(field).isJsonPrimitive()) {
      try {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(getString(jsonObject, field));
      } catch (ParseException e) {
        Log.e("JSON", "Error during the deserializer of a date field.");
      }
    }
    return defaultValue;
  }

  public static <T> T getObject(JsonObject jsonObject, String field, Class<T> clazz) {
    return getObject(jsonObject, field, clazz, null);
  }

  public static <T> T getObject(JsonObject jsonObject, String field, Class<T> clazz, T defaultValue) {
    if (jsonObject.get(field) != null && jsonObject.get(field).isJsonObject()) {
      return instantiateObjectFromJson(jsonObject.getAsJsonObject(field), clazz);
    } else if (jsonObject.get(field) != null && jsonObject.get(field).isJsonPrimitive()) {
      return instantiateObjectFromInteger(getInt(jsonObject, field), clazz);
    }
    return defaultValue;
  }

  public static <T> List<T> getList(JsonObject jsonObject, String field, Class<T> clazz) {
    if (jsonObject.get(field) != null && jsonObject.get(field).isJsonArray()) {
      final JsonArray jsonArray = jsonObject.get(field).getAsJsonArray();
      final List<T> res = new ArrayList<>();
      for (JsonElement element : jsonArray) {
        if (element.isJsonObject()) {
          res.add(instantiateObjectFromJson((JsonObject) element, clazz));
        } else if (element.isJsonPrimitive()) {
          res.add(instantiateObjectFromInteger(element.getAsInt(), clazz));
        }
      }
      return res;
    }
    return new ArrayList<>();
  }

  private static <T> T instantiateObjectFromJson(JsonObject jsonObject, Class<T> clazz) {
    try {
      return clazz.getDeclaredConstructor(JsonObject.class).newInstance(jsonObject);
    } catch (InstantiationException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    } catch (IllegalAccessException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    } catch (InvocationTargetException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    } catch (NoSuchMethodException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    }
    return null;
  }

  private static <T> T instantiateObjectFromInteger(int identifier, Class<T> clazz) {
    try {
      return clazz.getDeclaredConstructor(int.class).newInstance(identifier);
    } catch (InstantiationException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    } catch (IllegalAccessException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    } catch (InvocationTargetException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    } catch (NoSuchMethodException e) {
      Log.e("JSON", "Error during the deserializer of an object field.");
    }
    return null;
  }

  private JsonUtils() {
    throw new AssertionError("No instances.");
  }
}
