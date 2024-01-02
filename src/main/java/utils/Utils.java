package utils;

import java.util.List;

public class Utils {
  private Utils() {}

  public static Genre stringToGenre(String genre) {
    return switch (genre.toLowerCase()) {
      case "blues" -> Genre.BLUES;
      case "classical" -> Genre.CLASSICAL;
      case "country" -> Genre.COUNTRY;
      case "electronic" -> Genre.ELECTRONIC;
      case "folk" -> Genre.FOLK;
      case "hiphop" -> Genre.HIPHOP;
      case "jazz" -> Genre.JAZZ;
      case "reggae" -> Genre.REGGAE;
      case "rock" -> Genre.ROCK;
      case "r&b" -> Genre.RNB;
      case "pop" -> Genre.POP;
      default -> null;
    };
  }

  public static List<Genre> getEnumValues() {
    return List.of(Genre.values());
  }
}
