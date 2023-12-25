import commands.Commands;
import exceptions.NoItemPresentInTable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import queries.Queries;

import static helpers.DBHelper.cleanupDB;
import static helpers.DBHelper.setupDB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCommands {
  @BeforeAll
  public static void setup() {
    setupDB();
  }

  @AfterAll
  public static void cleanup() {
    cleanupDB();
  }

  @Test
  void testAddSongToPlaylistCommand() throws NoItemPresentInTable {
    Commands.addToPlaylist("user1", 1L);
    Commands.addToPlaylist("user1", 3L);

    var user = Queries.getUserByUsername("user1");
    var addedSong = Queries.getSongById(1L);

    assertEquals(2, user.getPlaylist().size());
    assertTrue(user.getPlaylist().stream().anyMatch(song -> song.equals(addedSong)));
  }
}
