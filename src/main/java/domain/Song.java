package domain;

import utils.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Song {
  private Long id;
  private String title;
  private List<String> artists;
  private Genre genre;
  private String length;
  private long streamCounter;

  private Map<String, Double> ratings = new HashMap<>();

  private Double averageRating;

  public Song(Long id, String title, List<String> artists, Genre genre, String length) {
    this.id = id;
    this.title = title;
    this.artists = artists;
    this.genre = genre;
    this.length = length;
    this.streamCounter = 0;
    this.averageRating = 0.0;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getArtists() {
    return artists;
  }

  public void setArtist(List<String> artists) {
    this.artists = artists;
  }

  public Genre getGenre() {
    return genre;
  }

  public void setGenre(Genre genre) {
    this.genre = genre;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }


  public long getStreamCounter() {
    return streamCounter;
  }

  public void setStreamCounter(long streamCounter) {
    this.streamCounter = streamCounter;
  }
  public Double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(Double averageRating) {
    this.averageRating = averageRating;
  }

  public void incrementStreamCounter() {
    streamCounter++;
  }

  public void setRatingByUser(String username, Double givenRating) {
    ratings.putIfAbsent(username, givenRating);

    computeFinalRating();
  }

  private void computeFinalRating() {
    var ratingSum = ratings.values().stream().reduce(0.0, Double::sum);
    var totalRatings = ratings.size();

    averageRating = ratingSum / totalRatings;
  }

  @Override
  public String toString() {
    return "Song{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", artists=" + artists +
        ", genre=" + genre +
        ", length='" + length + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Song song = (Song) o;
    return id.equals(song.id) && title.equals(song.title) && artists.equals(song.artists) && genre == song.genre;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, artists, genre);
  }

}
