import database.Database;
import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parser.JsonParser;
import utils.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatabasePopulationFromInput {

  List<String> artistsSong1 = List.of("Red Hot Chilli Peppers");
  List<String> artistsSong2 = List.of("2Pac", "Dr. Dre");

  @BeforeAll
  public static void setup() throws NoItemPresentInTable {
    JsonParser.parseInput("src/test/resources/inputParserTestFile.json");
  }

  @Test
    void testParsedSongs() {
    List<Song> songs = Database.getInstance().getSongTable();

    Song song1 = songs.get(0);
    Song song2 = songs.get(1);

    assertEquals(2, songs.size());

    assertEquals(6, song1.getId());
    assertEquals("Californication", song1.getTitle());
    assertArrayEquals(
        artistsSong1.toArray(),
        song1.getArtists().toArray());
    assertEquals(Genre.ROCK, song1.getGenre());
    assertEquals("05:33", song1.getLength());
    assertEquals(0, song1.getStreamCounter());

    assertEquals(8, song2.getId());
    assertEquals("California Love", song2.getTitle());
    assertArrayEquals(
        artistsSong2.toArray(),
        song2.getArtists().toArray());
    assertEquals(Genre.HIPHOP, song2.getGenre());
    assertEquals("04:22", song2.getLength());
    assertEquals(0, song2.getStreamCounter());
  }

  @Test
  void testParsedUsers() {
    List<User> users = Database.getInstance().getUserTable();

    assertEquals(3, users.size());

    User user1 = users.get(0);

    assertEquals("user-test", user1.getUsername());
    assertTrue(user1.getPlaylist().isEmpty());
  }

  @AfterAll
  public static void cleanup() {
    Database.getInstance().getUserTable().clear();
    Database.getInstance().getSongTable().clear();
  }
}
