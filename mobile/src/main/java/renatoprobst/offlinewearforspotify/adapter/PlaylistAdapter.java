package renatoprobst.offlinewearforspotify.adapter;

import android.content.Context;

import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistSimple;

/**
 * Created by Renato on 27/04/2016.
 */
public class PlaylistAdapter extends TwoLineGenericAdapter<PlaylistSimple> {

    public PlaylistAdapter(Context context, List<PlaylistSimple> list) {
        super(context, list);
    }


    @Override
    protected String getTitle(PlaylistSimple item) {
        return item.name;
    }

    @Override
    protected String getSubTitle(PlaylistSimple item) {
        return String.valueOf(item.tracks.total);
    }

}