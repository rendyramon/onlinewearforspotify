package renatoprobst.offlinewearforspotify.adapter;

import android.content.Context;

import java.util.List;

import kaaes.spotify.webapi.android.models.SavedAlbum;

/**
 * Created by Renato on 01/05/2016.
 */
public class AlbumAdapter extends TwoLineGenericAdapter<SavedAlbum> {
    public AlbumAdapter(Context context, List<SavedAlbum> list) {
        super(context, list);
    }

    @Override
    protected String getTitle(SavedAlbum item) {
        return item.album.name;
    }

    @Override
    protected String getSubTitle(SavedAlbum item) {
        if (item.album.artists == null || item.album.artists.size() == 0) {
            return "";
        }
        return item.album.artists.get(0).name;
    }
}
