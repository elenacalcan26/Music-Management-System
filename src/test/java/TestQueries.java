import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import queries.Queries;
import utils.Genre;

import static helpers.DBHelper.cleanupDB;
import static helpers.DBHelper.setupDB;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestQueries {

  @BeforeAll
  public static void setup() {
    setupDB();
  }

  @AfterAll
  public static void cleanup() {
    cleanupDB();
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
