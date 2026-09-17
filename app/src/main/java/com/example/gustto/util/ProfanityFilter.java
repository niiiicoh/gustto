package com.example.gustto.util;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public final class ProfanityFilter {
  private final List<Pattern> words = new ArrayList<>();

  public ProfanityFilter(Collection<String> list) {
    for (String s : list) {
      String n = normalize(s);
      if (!n.isEmpty() && !n.startsWith("#"))
        words.add(Pattern.compile("(?<![a-z0-9])" + Pattern.quote(n) + "(?![a-z0-9])"));
    }
  }

  public boolean blocked(String text) {
    String n = normalize(text);
    for (Pattern p : words) if (p.matcher(n).find()) return true;
    return false;
  }

  private static String normalize(String s) {
    return Normalizer.normalize(s, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "")
        .toLowerCase(Locale.ROOT)
        .replace('0', 'o')
        .replace('3', 'e')
        .replace('4', 'a')
        .replace('@', 'a');
  }
}
