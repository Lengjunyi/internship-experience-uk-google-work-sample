package com.google;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

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

  public void createPlaylist(String playlistName) {
    System.out.println("createPlaylist needs implementation");
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    System.out.println("addVideoToPlaylist needs implementation");
  }

  public void showAllPlaylists() {
    System.out.println("showAllPlaylists needs implementation");
  }

  public void showPlaylist(String playlistName) {
    System.out.println("showPlaylist needs implementation");
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    System.out.println("removeFromPlaylist needs implementation");
  }

  public void clearPlaylist(String playlistName) {
    System.out.println("clearPlaylist needs implementation");
  }

  public void deletePlaylist(String playlistName) {
    System.out.println("deletePlaylist needs implementation");
  }

  public void searchVideos(String searchTerm) {
    System.out.println("searchVideos needs implementation");
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
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