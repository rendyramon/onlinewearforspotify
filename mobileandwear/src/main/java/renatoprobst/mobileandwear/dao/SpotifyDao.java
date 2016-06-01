package renatoprobst.mobileandwear.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import renatoprobst.mobileandwear.service.ConnectionHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renato on 29/04/2016.
 */
public class SpotifyDao {

    private static final String TAG = "SpotifyDao";
    private SpotifyApi api;
    private SpotifyService service;
    Activity activity;
    private Context context;


    public SpotifyDao(Activity activity) {
        this.activity = activity;
        api = new SpotifyApi();
        String spotifyToken = ConnectionHelper.getToken(activity);
        Log.w(TAG, "token: " + spotifyToken);
        api.setAccessToken(spotifyToken);
        service = api.getService();
    }

    public SpotifyDao(Context context) {
        this.context = context;
        api = new SpotifyApi();
        String spotifyToken = ConnectionHelper.getToken(context);
        Log.w(TAG, "token: " + spotifyToken);
        api.setAccessToken(spotifyToken);
        service = api.getService();
    }

    public <G> Callback<G> getLoadingCallback(final Callback<G> callback) {

        final OnRequestWithLoading<G> onRequestWithLoading = new OnRequestWithLoading<G>(activity);
        onRequestWithLoading.beforeRequest();


        Callback<G> newCallback = new Callback<G>() {
            @Override
            public void success(G g, Response response) {
                onRequestWithLoading.onFinish();
                callback.success(g, response);
            }

            @Override
            public void failure(RetrofitError error) {
                onRequestWithLoading.onFinish();
                callback.failure(error);
            }
        };

        return newCallback;
    }

    public SpotifyService getService() {
        return service;
    }

    public void setService(SpotifyService service) {
        this.service = service;
    }


    public interface OnRequest<G> {
        void beforeRequest();
        void onFinish();
    }

    public class OnRequestWithLoading<G> implements OnRequest<G> {
        private Activity activity;
        private ProgressDialog dialog;

        public OnRequestWithLoading(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void beforeRequest() {
            showDialog();
        }

        @Override
        public void onFinish() {
            dismissDialog();
        }

        public void showDialog() {
            try {
                dialog = new ProgressDialog(activity);
                dialog.setMessage("Loading");
                dialog.show();
            }
            catch (Exception e) {}
        }

        public void dismissDialog() {
            try {
                dialog.dismiss();
            }
            catch (Exception e) {}
        }
    }
}
