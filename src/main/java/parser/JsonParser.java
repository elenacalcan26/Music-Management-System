package parser;

import commons.Constants;
import database.Database;
import domain.Song;
import domain.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("unchecked")
public class JsonParser {
  private JsonParser() {}

  public static void parseInput(String inputFile) {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;
    JSONObject jsonDB;
    JSONArray jsonSongs;
    JSONArray jsonUsers;

    try (FileReader reader = new FileReader(inputFile)) {

      jsonObject = (JSONObject) jsonParser.parse(reader);
      jsonDB = (JSONObject) jsonObject.get(Constants.DATABASE);
      jsonSongs = (JSONArray) jsonDB.get(Constants.MUSIC);
      jsonUsers = (JSONArray) jsonDB.get(Constants.USERS);

    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }

    parseSongs(jsonSongs);
    parseUsers(jsonUsers);
  }

  private static void parseSongs(JSONArray jsonSongs) {
    for (var jsonSong : jsonSongs) {
      JSONObject object = (JSONObject) jsonSong;

      Song song = new Song(
          (Long) object.get(Constants.ID),
          (String) object.get(Constants.TITLE),
          convertJSONArrayToStringList((JSONArray) object.get(Constants.ARTISTS)),
          Utils.stringToGenre((String) object.get(Constants.GENRE)),
          (String) object.get(Constants.LENGTH));

      Database.getInstance().getSongTable().add(song);
    }
  }

  private static void parseUsers(JSONArray jsonUsers) {
    for (var jsonUser : jsonUsers) {
      JSONObject jsonObject = (JSONObject) jsonUser;

      User user = new User((String) jsonObject.get(Constants.USERNAME));

      Database.getInstance().getUserTable().add(user);
    }
  }

  private static List<String> convertJSONArrayToStringList(JSONArray jsonArray) {
    return jsonArray.stream().map(String.class::cast).toList();
  }

}
