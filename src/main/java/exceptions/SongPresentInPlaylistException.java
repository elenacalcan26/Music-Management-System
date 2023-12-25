package exceptions;

public class SongPresentInPlaylistException extends  Exception {
  public SongPresentInPlaylistException(String message) {
    super(message);
  }
}
