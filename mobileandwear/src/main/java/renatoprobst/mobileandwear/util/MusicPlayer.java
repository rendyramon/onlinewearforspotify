package renatoprobst.mobileandwear.util;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.TrackSimple;

/**
 * Created by Renato on 29/04/2016.
 */
public class MusicPlayer {
    private int currentTime;
    private int totalTime;
    private List<TrackSimple> tracks = new ArrayList<>();
    private TrackSimple currentTrack;
    private String currentTrackName;
    private State state;

    public enum State {
        PAUSED, PLAYING, STOPED
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public List<TrackSimple> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackSimple> tracks) {
        this.tracks = tracks;
    }

    public TrackSimple getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(TrackSimple currentTrack) {
        this.currentTrack = currentTrack;
    }

    public String getCurrentTrackName() {
        return currentTrackName;
    }

    public void setCurrentTrackName(String currentTrackName) {
        this.currentTrackName = currentTrackName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
