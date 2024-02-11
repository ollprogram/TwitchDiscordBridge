package fr.ollprogram.twitchdiscordbridge.discord.utils;

import com.vdurmont.emoji.EmojiParser;

public class FilteringUtils {

  /**
   * @param s The text to be filtered
   * @return The same text without unknown emojis. And transform emojis to their unicode value.
   */
  public static String filterNonUniversalEmojis(String s) {
    String reference = EmojiParser.parseToUnicode(s); // reference will be unchanged
    String res = reference; // res will be modified
    StringBuilder emojiBuff = new StringBuilder();
    String emoji = null;
    boolean start = false;
    for (int i = 0; i < reference.length(); i++) {
      if (reference.charAt(i) == ' ') {
        start = false;
        emojiBuff = new StringBuilder();
      }
      if (reference.charAt(i) == ':') {
        if (!start) {
          start = true;
        } else {
          start = false;
          emojiBuff.append(reference.charAt(i));
          emoji = emojiBuff.toString();
          emojiBuff = new StringBuilder();
        }
      }
      if (start) {
        emojiBuff.append(reference.charAt(i));
      }
      if (emoji != null) {
        if (!emoji.equals("::")) res = res.replace(emoji, "");
        emoji = null;
      }
    }
    return res;
  }

  /**
   * @param s The string to analyse.
   * @return True if the string contains only spaces or nothing.
   */
  public static boolean isEmptyString(String s) {
    return s.replace(" ", "").equals("");
  }
}
