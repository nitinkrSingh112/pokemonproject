package com.example.pokedex.util;

import java.util.List;
import java.util.Map;

public class TextUtil {
  public static String englishText(List<Map<String, Object>> entries, String key) {
    if (entries == null) return "";
    for (var e : entries) {
      var lang = (Map<String, Object>) e.get("language");
      if (lang != null && "en".equals(lang.get("name"))) {
        var val = (String) e.get(key);
        if (val == null) return "";
        return val.replace("\n", " ").replace("\f", " ").trim();
      }
    }
    return "";
  }
}
