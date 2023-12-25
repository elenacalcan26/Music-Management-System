import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;
import exceptions.SongPresentInPlaylistException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import queries.Queries;
import utils.Genre;

import java.util.List;

import static helpers.DBHelper.cleanupDB;
import static helpers.DBHelper.setupDB;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCustomExceptions {
  @BeforeAll
  public static void setup() {
    setupDB();
  }

  @AfterAll
  public static void cleanup() {
    cleanupDB();
  }

  @Test
  void testNoItemPresentInUserTable() {
    assertThrows(NoItemPresentInTable.class,
        () -> Queries.getUserByUsername("nonExistentUser"));
  }

  @Test
  void testNoItemPresentInMusicTableWithId() {
    assertThrows(NoItemPresentInTable.class,
        () -> Queries.getSongById(999999L));
  }

  @Test
  void testSongPresentInPlaylist() throws SongPresentInPlaylistException {
    User user = new User("testUser");
    Song song = new Song(
        100L,
        "Smooth Operator",
        List.of("Sade"),
        Genre.JAZZ,
        "04:58");

    user.addSongToPlaylist(song);

    assertThrows(SongPresentInPlaylistException.class, () -> user.addSongToPlaylist(song));
  }

}
