package helpers;

import database.Database;
import domain.Song;
import domain.User;
import utils.Genre;

import java.util.List;

public class DBHelper {
  public static void setupDB() {
    setupMusicTable();
    setupUserTable();
  }

  public static void cleanupDB() {
    Database.getInstance().getUserTable().clear();
    Database.getInstance().getSongTable().clear();
  }

  private static void setupUserTable() {
    User user1 = new User("user1");
    User user2 = new User("user2");
    User user3 = new User("user3");

    List<User> users = List.of(user1, user2, user3);

    Database.getInstance().getUserTable().addAll(users);
  }

  private static void setupMusicTable() {
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

    Song song3 = new Song(
      3L,
      "Purple Rain",
      List.of("Prince"),
      Genre.ROCK,
        "04:05"
    );

    Song song4 = new Song(
        4L,
        "Fragments of Time",
        List.of("Daft Punk", "Todd Edwards"),
        Genre.ELECTRONIC,
        "04:40"
    );

    Song song5 = new Song(
        5L,
        "Infinity Repeating (2013 Demo)",
        List.of("Daft Punk", "Julian Casablanca"),
        Genre.ELECTRONIC,
        "03:59"
    );

    List<Song> songs = List.of(song1, song2, song3, song4, song5);

    Database.getInstance().getSongTable().addAll(songs);
  }
}
