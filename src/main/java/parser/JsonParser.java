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
import queries.Queries;
import utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public class JsonParser {
  private static final Logger logger = Logger.getLogger(JsonParser.class.getName());
  private static List<Runnable> tasks = new ArrayList<>();

  private JsonParser() {}

  public static void parseInput(String inputFile) throws InterruptedException {
    logger.info("Start parsing the given input!");

    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;
    JSONObject jsonDB;
    JSONArray jsonCommands;
    JSONArray jsonQueries;

    try (FileReader reader = new FileReader(inputFile)) {
      logger.info("Read the input file!");

      jsonObject = (JSONObject) jsonParser.parse(reader);

    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }

    jsonDB = (JSONObject) jsonObject.get(Constants.DATABASE);
    jsonCommands = (JSONArray) jsonObject.get(Constants.COMMANDS);
    jsonQueries = (JSONArray) jsonObject.get(Constants.QUERIES);

    parseDatabase(jsonDB);
    parseCommands(jsonCommands);
    parseQueries(jsonQueries);

    List<Thread> threads = new ArrayList<>();
    tasks.forEach(t -> threads.add(new Thread(t)));

    threads.forEach(Thread::start);

    for (Thread thread : threads) {
      thread.join();
    }
  }

  private static void parseDatabase(JSONObject jsonDB) {
    logger.info("Parse the database!");

    JSONArray jsonSongs = (JSONArray) jsonDB.get(Constants.MUSIC);
    JSONArray jsonUsers = (JSONArray) jsonDB.get(Constants.USERS);

    parseSongs(jsonSongs);
    parseUsers(jsonUsers);
  }

  private static void parseCommands(JSONArray jsonCommands) {
    logger.info("Get commands!");

    for (var item : jsonCommands) {
      JSONObject command = (JSONObject) item;
      String commandName = (String) command.get(Constants.NAME);

      Runnable commandTask = getCommandTask(commandName, command);
      tasks.add(commandTask);
    }
  }

  private static void parseQueries(JSONArray jsonQueries) {
    logger.info("Get Queries!");

    for (var item : jsonQueries) {
      JSONObject query = (JSONObject) item;
      String queryName = (String) query.get(Constants.NAME);

      switch (queryName) {
        case Constants.GET_ALL_ARTISTS ->
            tasks.add(() -> System.out.println(Queries.getAllArtists()));

        case Constants.GROUP_SONGS_BY_ARTIST ->
            tasks.add(() -> System.out.println(Queries.groupSongsByArtist()));

        case Constants.GROUP_SONGS_BY_GENRE ->
            tasks.add(() -> System.out.println(Queries.groupSongsByGenre()));
        case Constants.GET_SONG_BY_ARTIST ->
            tasks.add(() -> System.out.println(Queries.getSongsByArtist((String) query.get(Constants.ARTIST))));

        case Constants.ORDER_SONGS_BY_STREAM_COUNTER ->
            tasks.add(() -> Queries.orderSongsBy(Comparator.comparing(Song::getStreamCounter)));

        case Constants.ORDER_SONGS_BY_RATING ->
            tasks.add(() -> Queries.orderSongsBy(Comparator.comparing(Song::getAverageRating)));

        case Constants.GET_FAVORITE_SONGS -> tasks.add(() -> System.out.println(Queries.getFavoriteSongs()));

        default -> logger.info("Unknown query!");
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

  private static Runnable getCommandTask(String commandName, JSONObject command) {
    switch (commandName) {
      case Constants.ADD_TO_PLAYLIST:
        return createTask(() -> Commands.addToPlaylist(
            (String) command.get(Constants.USERNAME), (Long) command.get(Constants.SONG_ID)));

      case Constants.PLAY_SONG:
        return createTask(() -> Commands.playSong(
            (Long) command.get(Constants.SONG_ID)));

      case Constants.PLAY_SONGS_FROM_PLAYLIST:
        return createTask(() -> Commands.playAllSongsFromPlaylist(
            (String) command.get(Constants.USERNAME)));

      case Constants.RATE_SONG:
        return createTask(() -> Commands.rateSong(
            (String) command.get(Constants.USERNAME), (Long) command.get(Constants.SONG_ID),
            (double) command.get(Constants.RATE)));
      default:
        logger.info("Unknown command!");

        return null;
    }
  }

  private static Runnable createTask(CommandExecution commandExecution) {
    return () -> {
      try {
        commandExecution.execute();
      } catch (NoItemPresentInTable e) {
        e.printStackTrace();
      }
    };
  }

  @FunctionalInterface
  interface CommandExecution {
    void execute() throws NoItemPresentInTable;
  }

}
