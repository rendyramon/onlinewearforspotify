package renatoprobst.mobileandwear.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.spotify.sdk.android.player.ConnectionStateCallback;

/**
 * Created by Renato on 30/04/2016.
 */
public class ConnectionHelper implements ConnectionStateCallback {
    public static final String CLIENT_ID = "03be19c80b4f4011af1472128bee37e4";
    private static final String TAG = "ConnectionHelper";

    public static String getToken(Context context) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        String token = sharedPref.getString("spotifyToken", null);
        return token;
    }

    public static void saveToken(Context context, String spotifyToken) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("spotifyToken", spotifyToken);
        editor.commit();
    }

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d(TAG, "Received connection message: " + message);
    }
}
