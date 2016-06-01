package renatoprobst.offlinewearforspotify.adapter;

import android.content.Context;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Renato on 01/05/2016.
 */
public class TrackAdapter extends TwoLineGenericAdapter<Track> {
    public TrackAdapter(Context context, List<Track> list) {
        super(context, list);
    }

    @Override
    protected String getTitle(Track item) {
        return item.album.name;
    }

    @Override
    protected String getSubTitle(Track item) {
        String album = item.album != null ? item.album.name : "";
        String artist = item.artists == null || item.artists.size() == 0 ? "" :item.artists.get(0).name;


        return artist + " - " + album;
    }
}
