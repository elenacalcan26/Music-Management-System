import commands.Commands;
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

  @Test
  void testGetAllArtists() {
    var artists = Queries.getAllArtists();

    assertEquals(6, artists.size());
  }

  @Test
  void testGroupSongsByArtists() {
    var songsByArtist = Queries.groupSongsByArtist();

    assertEquals(6, songsByArtist.size());
    assertEquals(2, songsByArtist.get("Daft Punk").size());
  }

  @Test
  void testGroupSongsByGenre() {
    var songsByGenre = Queries.groupSongsByGenre();

    assertEquals(3, songsByGenre.size());
    assertEquals(2, songsByGenre.get(Genre.ROCK).size());
  }

  @Test
  void testGetSongsByArtist() {
    var songsByArtist = Queries.getSongsByArtist("Daft Punk");

    assertEquals(2, songsByArtist.size());
  }

  @Test
  void testOrderSongsByStreamingCounter() throws NoItemPresentInTable {
    Commands.addToPlaylist("user1", 1L);
    Commands.addToPlaylist("user1", 2L);
    Commands.addToPlaylist("user2", 2L);
    Commands.addToPlaylist("user2", 3L);
    Commands.addToPlaylist("user3", 3L);
    Commands.addToPlaylist("user3", 2L);

    Commands.playAllSongsFromPlaylist("user1");
    Commands.playAllSongsFromPlaylist("user2");
    Commands.playAllSongsFromPlaylist("user3");

    var orderedSongs = Queries.orderSongsByStreamCounter();

    assertEquals(2L, orderedSongs.get(0).getId());
  }

  @Test
  void testOrderSongsByRating() throws NoItemPresentInTable {
    Commands.rateSong("user1", 3L, 10.0);
    Commands.rateSong("user1", 1L, 7.0);
    Commands.rateSong("user1", 5L, 8.5);
    Commands.rateSong("user2", 1L, 5.0);
    Commands.rateSong("user2", 5L, 6.5);
    Commands.rateSong("user3", 2L, 9.5);
    Commands.rateSong("user3", 4L, 8.0);
    Commands.rateSong("user3", 3L, 9.5);

    var orderedSongs = Queries.orderSongsByRating();

    assertEquals(3L, orderedSongs.get(0).getId());
  }
}
