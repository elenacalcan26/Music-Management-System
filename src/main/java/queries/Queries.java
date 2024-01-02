package queries;

import database.Database;
import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;

import java.util.Collection;
import java.util.List;

public class Queries {
  private Queries() {}

  public static User getUserByUsername(String username) throws NoItemPresentInTable {
    var user = Database
        .getInstance()
        .getUserTable()
        .stream()
        .filter(e -> e.getUsername().equals(username))
        .findFirst();

    if (user.isEmpty()) {
      throw new NoItemPresentInTable();
    }

    return user.get();
  }

  public static Song getSongById(Long id) throws NoItemPresentInTable {
    var song = Database
        .getInstance()
        .getSongTable()
        .stream()
        .filter(e -> e.getId().equals(id))
        .findFirst();

    if (song.isEmpty()) {
      throw new NoItemPresentInTable();
    }

    return song.get();
  }

  public static List<String> getAllArtists() {
   return Database
       .getInstance()
       .getSongTable()
       .stream()
       .map(Song::getArtists)
       .flatMap(Collection::stream)
       .distinct()
       .toList();
  }
}
