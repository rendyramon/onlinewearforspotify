package renatoprobst.offlinewearforspotify.activity;

import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import renatoprobst.offlinewearforspotify.activity.generic.GenericSpotifyListActivity;
import renatoprobst.offlinewearforspotify.adapter.AlbumAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AlbumListUserActivity extends GenericSpotifyListActivity<SavedAlbum> {

    @Override
    public void getData() {
        spotifyDao.getService().getMySavedAlbums(spotifyDao.getLoadingCallback(new Callback<Pager<SavedAlbum>>() {
            @Override
            public void success(Pager<SavedAlbum> playlistSimplePager, Response response) {
                list = playlistSimplePager.items;
                listAdapter = new AlbumAdapter(AlbumListUserActivity.this, list);
                setData();
            }

            @Override
            public void failure(RetrofitError error) {
                logRetrofitError(error);
            }
        }));
    }

}
