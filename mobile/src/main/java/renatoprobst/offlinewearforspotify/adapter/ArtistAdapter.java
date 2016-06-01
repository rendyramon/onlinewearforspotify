package renatoprobst.offlinewearforspotify.adapter;

import android.content.Context;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Renato on 27/04/2016.
 */
public class ArtistAdapter extends TwoLineGenericAdapter<Artist> {

    public ArtistAdapter(Context context, List<Artist> list) {
        super(context, list);
    }


    @Override
    protected String getTitle(Artist item) {
        return item.name;
    }

    @Override
    protected String getSubTitle(Artist item) {


        return "";
    }

}