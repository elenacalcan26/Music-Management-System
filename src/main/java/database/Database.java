package database;

import domain.Song;
import domain.User;

import java.util.ArrayList;
import java.util.List;

public class Database {

  private final List<Song> songTable = new ArrayList<>();
  private final List<User> userTable = new ArrayList<>();

  private static Database instance;

  private Database() { }

  // TODO: make it thread safe: https://www.digitalocean.com/community/tutorials/java-singleton-design-pattern-best-practices-examples#4-thread-safe-singleton
  public static Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }

    return instance;
  }

  public List<Song> getSongTable() {
    return songTable;
  }

  public List<User> getUserTable() {
    return userTable;
  }
}
