package domain;

import exceptions.SongPresentInPlaylistException;

import java.util.*;
import java.util.logging.Logger;

public class User {
  private String username;
  private Set<Song> playlist = new LinkedHashSet<>();

  public User(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  private static Logger logger = Logger.getLogger(User.class.getName());

  public Set<Song> getPlaylist() {
    return Collections.unmodifiableSet(playlist);
  }

  public void addSongToPlaylist(Song song) throws SongPresentInPlaylistException {
    if (playlist.contains(song)) {
      throw new SongPresentInPlaylistException("Song already exists in playlist");
    }

    playlist.add(song);

    logger.info("Song with id = " + song.getId() + " is added to user " + username + " playlist!");
  }

  @Override
  public String toString() {
    return "User{" +
        "username='" + username + '\'' +
        ", playlist=" + playlist +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return username.equals(user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }
}
