package domain;

import java.util.*;

public class User {
  private String username;
  private Set<Song> playlist = new LinkedHashSet<>();

  public User(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<Song> getPlaylist() {
    return Collections.unmodifiableSet(playlist);
  }

  public void addSongToPlaylist(Song song) {
    if (playlist.contains(song)) {
      System.out.println("Nope");
    }

    playlist.add(song);
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
