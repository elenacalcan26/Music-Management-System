package commands;

import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;
import exceptions.SongPresentInPlaylistException;
import queries.Queries;

public class Commands {
  private Commands() {}

  public static void addToPlaylist(String username, Long songId) throws NoItemPresentInTable {
    User user = Queries.getUserByUsername(username);
    Song song = Queries.getSongById(songId);

    try {
      user.addSongToPlaylist(song);
    } catch (SongPresentInPlaylistException e) {
      System.out.format("Song with id = %d is already present in user = %s playlist", songId, username);
    }
  }

  public static void playSong(Long songId) throws NoItemPresentInTable {
    Song song = Queries.getSongById(songId);

    song.incrementStreamCounter();
  }

  public static void playAllSongsFromPlaylist() {}

  public static void rateSong() {}
}
