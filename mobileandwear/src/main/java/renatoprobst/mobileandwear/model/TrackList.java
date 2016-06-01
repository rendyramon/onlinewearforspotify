package renatoprobst.mobileandwear.model;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Renato on 01/05/2016.
 */
public class TrackList extends MyModel {
    Type type;
    List<Track> tracks = new ArrayList<>();
    Track currentTrack;

    public TrackList(Type track, List<Track> list, Track currentTrack) {

        type = track;
        tracks = list;
        this.currentTrack = currentTrack;
    }

    public enum Type {
        Track, TrackSimple
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }
}
