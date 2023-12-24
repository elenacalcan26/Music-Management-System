import database.Database;
import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import queries.Queries;
import utils.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestQueries {

  @BeforeAll
  public static void setup() {
    setupUserDB();
    setupMusicDB();
  }

  private static void setupUserDB() {
    User user1 = new User("user1");
    User user2 = new User("user2");
    List<User> users = List.of(user1, user2);

    Database.getInstance().getUserTable().addAll(users);
  }

  private static void setupMusicDB() {
    Song song1 = new Song(
        1L,
        "Californication",
        List.of("Red Hot Chilli Peppers"),
        Genre.ROCK,
        "05:33");

    Song song2 = new Song(
      2L,
      "Without Me",
      List.of("Eminem"),
      Genre.HIPHOP,
      "04:50"
    );

    List<Song> songs = List.of(song1, song2);

    Database.getInstance().getSongTable().addAll(songs);
  }

  @AfterAll
  public static void cleanup() {
    Database.getInstance().getUserTable().clear();
  }

  @Test
  void testQueryUserByUsername() throws NoItemPresentInTable {
    User queriedUser = Queries.getUserByUsername("user1");

    assertEquals("user1", queriedUser.getUsername());
  }

  @Test
  void testQuerySongById() throws NoItemPresentInTable {
    Song queriedSong = Queries.getSongById(2L);

    assertEquals(2L, queriedSong.getId());
    assertEquals("Without Me", queriedSong.getTitle());
    assertEquals(Genre.HIPHOP, queriedSong.getGenre());
  }
}
