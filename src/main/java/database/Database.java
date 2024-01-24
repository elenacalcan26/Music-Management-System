package database;

import domain.Song;
import domain.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {

  private final List<Song> songTable = Collections.synchronizedList(new ArrayList<>());
  private final List<User> userTable = Collections.synchronizedList(new ArrayList<>());

  private static volatile Database instance;

  private Database() { }

  public static Database getInstance() {
    if (instance == null) {
      synchronized (Database.class) {
        if (instance == null) {
          instance = new Database();
        }
      }
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
