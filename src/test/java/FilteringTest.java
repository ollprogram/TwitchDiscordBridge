import static org.junit.Assert.*;

import fr.ollprogram.twitchdiscordbridge.discord.utils.FilteringUtils;
import org.junit.Test;

public class FilteringTest {

  @Test
  public void oneEmoji1() {
    String source = ":grinning:";
    assertEquals("\uD83D\uDE00", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void oneEmoji2() {
    String source = ":jcvjshvsjbc:";
    assertEquals("", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void oneEmoji3() {
    String source = ":jcvjsh vsjbc:";
    assertEquals(":jcvjsh vsjbc:", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void oneEmoji4() {
    String source = "::";
    assertEquals("::", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void oneEmoji5() {
    String source = "jcvjshv:sjbc";
    assertEquals("jcvjshv:sjbc", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void someEmojis1() {
    String source = ":smiley: :grinning:";
    assertEquals("\uD83D\uDE03 \uD83D\uDE00", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void someEmojis2() {
    String source = ":smiley::grinning:";
    assertEquals("\uD83D\uDE03\uD83D\uDE00", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void someEmojis3() {
    String source = ":smiley: :gggg:";
    assertEquals("\uD83D\uDE03 ", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void someEmojis4() {
    String source = ":gggd: :gggg:";
    assertEquals(" ", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void someEmojis5() {
    String source = ":smiley::gggg:";
    assertEquals("\uD83D\uDE03", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void someEmojis6() {
    String source = ":smiley::gggg::smiley:";
    assertEquals("\uD83D\uDE03\uD83D\uDE03", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void someEmojis7() {
    String source = ":smiley:joy:grinning:";
    assertEquals("\uD83D\uDE03joy\uD83D\uDE00", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void normal1() {
    String source = ":smiley: Coucou! Comment vas-tu?";
    assertEquals(
        "\uD83D\uDE03 Coucou! Comment vas-tu?", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void normal2() {
    String source = ":smiley: Coucou! Comment:fdfbs: vas-tu?";
    assertEquals(
        "\uD83D\uDE03 Coucou! Comment vas-tu?", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void normal3() {
    String source = "Coucou! Comment vas-tu? :smiley:";
    assertEquals(
        "Coucou! Comment vas-tu? \uD83D\uDE03", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void normal4() {
    String source = "Coucou! :smiley: Comment vas-tu?";
    assertEquals(
        "Coucou! \uD83D\uDE03 Comment vas-tu?", FilteringUtils.filterNonUniversalEmojis(source));
  }

  @Test
  public void spaces() {
    assertTrue(FilteringUtils.isEmptyString(""));
    assertTrue(FilteringUtils.isEmptyString("  "));
    assertTrue(FilteringUtils.isEmptyString("   "));
    assertTrue(FilteringUtils.isEmptyString("            "));
    assertFalse(FilteringUtils.isEmptyString(" a "));
    assertFalse(FilteringUtils.isEmptyString("a"));
    assertFalse(FilteringUtils.isEmptyString("a "));
    assertFalse(FilteringUtils.isEmptyString(" a"));
  }
}
