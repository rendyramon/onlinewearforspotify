package renatoprobst.offlinewearforspotify.activity;

import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import renatoprobst.offlinewearforspotify.activity.generic.GenericSpotifyListActivity;
import renatoprobst.offlinewearforspotify.adapter.PlaylistAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistListUserActivity extends GenericSpotifyListActivity<PlaylistSimple> {

    @Override
    public void getData() {
        spotifyDao.getService().getMyPlaylists(spotifyDao.getLoadingCallback(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                list = playlistSimplePager.items;
                listAdapter = new PlaylistAdapter(PlaylistListUserActivity.this, list);
                setData();
            }

            @Override
            public void failure(RetrofitError error) {
                logRetrofitError(error);
            }
        }));
    }

}
