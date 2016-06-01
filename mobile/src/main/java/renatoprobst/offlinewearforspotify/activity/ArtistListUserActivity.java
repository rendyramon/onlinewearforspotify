package renatoprobst.offlinewearforspotify.activity;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Pager;
import renatoprobst.offlinewearforspotify.activity.generic.GenericSpotifyListActivity;
import renatoprobst.offlinewearforspotify.adapter.ArtistAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistListUserActivity extends GenericSpotifyListActivity<Artist> {

    @Override
    public void getData() {
        spotifyDao.getService().getTopArtists(spotifyDao.getLoadingCallback(new Callback<Pager<Artist>>() {
            @Override
            public void success(Pager<Artist> playlistSimplePager, Response response) {
                list = playlistSimplePager.items;
                listAdapter = new ArtistAdapter(ArtistListUserActivity.this, list);
                setData();
            }

            @Override
            public void failure(RetrofitError error) {
                logRetrofitError(error);
            }
        }));
    }

}
