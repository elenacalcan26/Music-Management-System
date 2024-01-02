package queries;

import database.Database;
import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public static Map<String, List<String>> groupSongsByArtist() {
    List<String> artists = getAllArtists();
    Map<String, List<String>> songsByArtist = new HashMap<>();

    artists.forEach(artist -> songsByArtist.put(artist,
        Database.getInstance()
            .getSongTable()
            .stream()
            .filter(e -> e.getArtists().contains(artist))
            .map(Song::getTitle)
            .toList()));

    return songsByArtist;
  }
}
