package com.bobax.product_service.util;

public class SlugUtil {
  public static String slug(String texto) {
    if (texto == null) return null;
    String s = texto.trim().toLowerCase()
      .replaceAll("[áàä]", "a")
      .replaceAll("[éèë]", "e")
      .replaceAll("[íìï]", "i")
      .replaceAll("[óòö]", "o")
      .replaceAll("[úùü]", "u")
      .replaceAll("[^a-z0-9\\s-]", "")
      .replaceAll("\\s+", "-")
      .replaceAll("-{2,}", "-");
    return s;
  }
}
