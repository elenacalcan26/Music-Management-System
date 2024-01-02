import commands.Commands;
import domain.Song;
import exceptions.NoItemPresentInTable;
import exceptions.SongPresentInPlaylistException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import queries.Queries;
import utils.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static helpers.DBHelper.cleanupDB;
import static helpers.DBHelper.setupDB;
import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  void testPlaySong() throws NoItemPresentInTable {
    for (int i = 0; i < 5; i++) {
      Commands.playSong(2L);
    }

    Song streamedSong = Queries.getSongById(2L);

    assertEquals(5, streamedSong.getStreamCounter());
  }

  @Test
  void testPlaySongsFromPlaylist() throws NoItemPresentInTable, SongPresentInPlaylistException {
    Song song1 = new Song(100L, "Paradise", List.of("Sade"), Genre.JAZZ, "03:37");
    Song song2 = new Song(101L, "Frozen", List.of("Madonna"), Genre.POP, "06:07");
    Song song3 = new Song(102L, "Low", List.of("Lenny Kravitz"), Genre.ROCK, "05:19");

    song1.incrementStreamCounter();
    song2.incrementStreamCounter();
    song2.incrementStreamCounter();

    var user = Queries.getUserByUsername("user3");
    user.addSongToPlaylist(song1);
    user.addSongToPlaylist(song2);
    user.addSongToPlaylist(song3);

    List<Long> expectedStreamCounters = List.of(2L, 3L, 1L);

    Commands.playAllSongsFromPlaylist("user3");

    assertIterableEquals(
        expectedStreamCounters,
        user.getPlaylist().stream().map(Song::getStreamCounter).collect(Collectors.toList()));
  }

  @Test
  void testRateSongCommand() throws NoItemPresentInTable {
    Commands.rateSong("user1", 1L, 9.0);
    Commands.rateSong("user2", 1L, 7.5);
    Commands.rateSong("user3", 1L, 6.0);

    Song song = Queries.getSongById(1L);

    assertEquals(7.5, song.getAverageRating());
  }
}

