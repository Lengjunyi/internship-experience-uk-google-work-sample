package com.google;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video playingVideo;
  private boolean paused = false;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  private String videoDetail(Video video) {
    return String.format("%s (%s) [%s]",
            video.getTitle(),
            video.getVideoId(),
            video.getTags().stream().reduce(
                    (t, s) -> t + " " + s
            ).orElse(""));
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videos = videoLibrary.getVideos();
    videos.sort(Comparator.comparing(Video::getTitle));
    for (Video video : videos) {
      System.out.println(videoDetail(video));
    }
  }

  private void stopVideoIfPlaying() {
    if (playingVideo != null) {
      System.out.printf("Stopping video: %s%n", playingVideo.getTitle());
    }
    playingVideo = null;
  }

  private void playNewVideo(Video video) {
    assert video != null;
    System.out.printf("Playing video: %s%n", video.getTitle());
    playingVideo = video;
    paused = false;
  }

  public void playVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video != null) {
      stopVideoIfPlaying();
      playNewVideo(video);
    } else {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if (playingVideo != null) {
      System.out.printf("Stopping video: %s%n", playingVideo.getTitle());
      playingVideo = null;
    } else {
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  Random generator = new Random();

  public void playRandomVideo() {
    stopVideoIfPlaying();
    List<Video> videos = videoLibrary.getVideos();
    int index = generator.nextInt(videos.size());
    playNewVideo(videos.get(index));
  }

  public void pauseVideo() {
    if (playingVideo == null) {
      System.out.println("Cannot pause video: No video is currently playing");
    } else {
      String videoName = playingVideo.getTitle();
      if (paused) {
        System.out.printf("Video already paused: %s%n", videoName);
      } else {
        System.out.printf("Pausing video: %s%n", videoName);
        paused = true;
      }
    }
  }

  public void continueVideo() {
    if (playingVideo == null) {
      System.out.println("Cannot continue video: No video is currently playing");
    } else {
      String videoName = playingVideo.getTitle();
      if (paused) {
        System.out.printf("Continuing video: %s%n", videoName);
        paused = false;
      } else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    }
  }

  public void showPlaying() {
    if (playingVideo == null) {
      System.out.println("No video is currently playing");
    } else {
      System.out.println("Currently playing: " +
              videoDetail(playingVideo) +
              (paused ? " - PAUSED" : ""));
    }
  }

  private static class PlayList {
    private final String name;
    private List<Video> videos;

    private PlayList(String name) {
      this.name = name;
      this.videos = new ArrayList<>();
    }

    public boolean addVideo(Video video) {
      if (videos.contains(video)) {
        return false;
      } else {
        videos.add(video);
        return true;
      }
    }

    public boolean removeVideo(Video video) {
      if (videos.contains(video)) {
        videos.remove(video);
        return true;
      } else {
        return false;
      }
    }
  }

  Map<String, PlayList> playListMap = new HashMap<>();

  public void createPlaylist(String playlistName) {
    String lowerCaseName = playlistName.toLowerCase();
    if (playListMap.containsKey(lowerCaseName)) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    } else {
      playListMap.put(lowerCaseName, new PlayList(playlistName));
      System.out.println("Successfully created new playlist: " + playlistName);
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    String lowerCaseName = playlistName.toLowerCase();
    if (playListMap.containsKey(lowerCaseName)) {
      Video video = videoLibrary.getVideo(videoId);
      PlayList playList = playListMap.get(lowerCaseName);
      if (video != null) {
        if (playList.addVideo(video)) {
          System.out.printf("Added video to %s: %s%n", playlistName, video.getTitle());
        } else {
          System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
        }
      } else {
        System.out.printf("Cannot add video to %s: Video does not exist%n", playlistName);
      }
    } else {
      System.out.printf("Cannot add video to %s: Playlist does not exist%n", playlistName);
    }
  }

  public void showAllPlaylists() {
    List<String> lowerCaseNames = new ArrayList<>(playListMap.keySet());
    lowerCaseNames.sort(CharSequence::compare);
    if (lowerCaseNames.isEmpty()) {
      System.out.println("No playlists exist yet");
    } else {
      System.out.println("Showing all playlists:");
      lowerCaseNames.forEach(
              n -> System.out.println(playListMap.get(n).name)
      );
    }
  }

  public void showPlaylist(String playlistName) {
    PlayList playList = playListMap.get(playlistName.toLowerCase());
    if (playList != null) {
      System.out.printf("Showing playlist: %s%n", playlistName);
      if (playList.videos.isEmpty()) {
        System.out.println("  No videos here yet");
      } else {
        playList.videos.forEach(
                v -> System.out.println("  " + videoDetail(v))
        );
      }
    } else {
      System.out.printf("Cannot show playlist %s: Playlist does not exist%n", playlistName);
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    String lowerCaseName = playlistName.toLowerCase();
    PlayList playList = playListMap.get(lowerCaseName);
    if (playList != null) {
      Video video = videoLibrary.getVideo(videoId);
      if (video != null) {
        if (playList.removeVideo(video)) {
          System.out.printf("Removed video from %s: %s%n", playlistName, video.getTitle());
        } else {
          System.out.printf("Cannot remove video from %s: Video is not in playlist%n", playlistName);
        }
      } else {
        System.out.printf("Cannot remove video from %s: Video does not exist%n", playlistName);
      }
    } else {
      System.out.printf("Cannot remove video from %s: Playlist does not exist%n", playlistName);
    }
  }

  public void clearPlaylist(String playlistName) {
    String lowerCaseName = playlistName.toLowerCase();
    PlayList playList = playListMap.get(lowerCaseName);
    if (playList != null) {
      playList.videos = new ArrayList<>();
      System.out.println("Successfully removed all videos from " + playlistName);
    } else {
      System.out.printf("Cannot clear playlist %s: Playlist does not exist%n", playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    String lowerCaseName = playlistName.toLowerCase();
    if (playListMap.containsKey(lowerCaseName)) {
      playListMap.remove(lowerCaseName);
      System.out.println("Deleted playlist: " + playlistName);
    } else {
      System.out.printf("Cannot delete playlist %s: Playlist does not exist%n", playlistName);
    }
  }

  private void searchVideosBy(Predicate<Video> function, String searchString) {
    List<Video> videos = videoLibrary.getVideos().stream()
            .filter(function)
            .sorted(Comparator.comparing(Video::getTitle))
            .collect(Collectors.toList());
    if (videos.isEmpty()) {
      System.out.println("No search results for " + searchString);
    } else {
      System.out.printf("Here are the results for %s:%n", searchString);
      AtomicInteger number = new AtomicInteger();
      videos.forEach(x -> {
        number.getAndIncrement();
        System.out.println(number + ") " + videoDetail(x));
      });
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n" +
              "If your answer is not a valid number, we will assume it's a no.");
      try {
        int answer = new Scanner(System.in).nextInt() - 1;
        if (answer >= 0 && answer < videos.size()) {
          playVideo(videos.get(answer).getVideoId());
        }
      } catch (InputMismatchException ignored) {
      }
    }
  }

  public void searchVideos(String searchTerm) {
    searchVideosBy(x -> x.getTitle().toLowerCase().contains(searchTerm.toLowerCase()), searchTerm);
  }

  public void searchVideosWithTag(String videoTag) {
    searchVideosBy(x -> x.getTags().stream()
                    .anyMatch(t -> t.toLowerCase().contains(videoTag.toLowerCase()))
    , videoTag);
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}