package com.zestedesavoir.zdsnotificateur.internal.exceptions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gerard Paligot
 */
public final class ErrorResponse {
  private final Map<String, String> errors = new HashMap<>();

  public ErrorResponse(String jsonError) {
    final JsonElement json = new JsonParser().parse(jsonError);
    for (Map.Entry<String, JsonElement> errors : ((JsonObject) json).entrySet()) {
      add(errors.getKey(), (JsonArray) errors.getValue());
    }
  }

  private void add(String key, JsonArray messages) {
    for (JsonElement message : messages) {
      add(key, message.getAsString());
    }
  }

  private void add(String key, String message) {
    String messages = "";
    if (errors.containsKey(key)) {
      messages = errors.get(key) + ", ";
    }
    messages += message;
    errors.put(key, messages);
  }

  public String get(String key) {
    return errors.get(key);
  }

  public void set(String key, String message) {
    errors.put(key, message);
  }
}