package parser;

import commands.Commands;
import commons.Constants;
import database.Database;
import domain.Song;
import domain.User;
import exceptions.NoItemPresentInTable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public class JsonParser {
  private static Logger logger = Logger.getLogger(JsonParser.class.getName());

  private JsonParser() {}

  public static void parseInput(String inputFile) throws NoItemPresentInTable {
    logger.info("Start parsing the given input!");

    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;
    JSONObject jsonDB;
    JSONArray jsonCommands;

    try (FileReader reader = new FileReader(inputFile)) {
      logger.info("Read the input file!");

      jsonObject = (JSONObject) jsonParser.parse(reader);

    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }

    jsonDB = (JSONObject) jsonObject.get(Constants.DATABASE);
    jsonCommands = (JSONArray) jsonObject.get(Constants.COMMANDS);

    parseDatabase(jsonDB);
    parseCommands(jsonCommands);
  }

  private static void parseDatabase(JSONObject jsonDB) {
    logger.info("Parse the database!");

    JSONArray jsonSongs = (JSONArray) jsonDB.get(Constants.MUSIC);
    JSONArray jsonUsers = (JSONArray) jsonDB.get(Constants.USERS);

    parseSongs(jsonSongs);
    parseUsers(jsonUsers);
  }

  private static void parseCommands(JSONArray jsonCommands) throws NoItemPresentInTable {
    logger.info("Get commands!");

    for (var item : jsonCommands) {
      JSONObject command = (JSONObject) item;
      String commandName = (String) command.get(Constants.COMMAND_NAME);

      switch (commandName) {
        case Constants.ADD_TO_PLAYLIST -> Commands.addToPlaylist(
            (String) command.get(Constants.USERNAME), (Long) command.get(Constants.SONG_ID));
        case Constants.PLAY_SONG -> Commands.playSong();
        case Constants.PLAY_SONGS_FROM_PLAYLIST -> Commands.playAllSongsFromPlaylist();
        case Constants.RATE_SONG -> Commands.rateSong();
        default -> logger.info("Unknown command!");
      }
    }
  }

  private static void parseSongs(JSONArray jsonSongs) {
    logger.info("Get music data!");

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
    logger.info("Get users data!");

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
