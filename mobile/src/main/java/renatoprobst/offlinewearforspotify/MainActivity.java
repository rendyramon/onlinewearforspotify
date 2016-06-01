package renatoprobst.offlinewearforspotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.List;

import renatoprobst.mobileandwear.service.ConnectionHelper;
import renatoprobst.offlinewearforspotify.activity.UserMusicActivity;

public class MainActivity extends AppCompatActivity {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "03be19c80b4f4011af1472128bee37e4";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "renatoprobst.offlinewearforspotify://callback";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1337;
    private static final String TAG = "Mobile activity";
    private String spotifyToken;
    private GoogleApiClient mGoogleApiClient;

    private static final String GET_TOKEN_PATH = "/get-token-path-wear";
    private List<Node> myNodes;
    private String[] scopes = new String[]{"user-read-private", "user-top-read", "streaming", "playlist-read-private", "playlist-read-collaborative",
            "user-library-read"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(scopes);
        AuthenticationRequest request = builder.build();

        Log.w(TAG, "opening login activity");
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Log.w(TAG, "spotify token retrieved");
                spotifyToken = response.getAccessToken();
                startGPlayServices();

                //testPlayer();

            }
            else {
                Log.w(TAG, "autentication error " + response.getCode() + " " + response.getError());
            }
        }
        else {
            Log.w(TAG, "REQUEST_CODE error");
        }

    }

    private void testPlayer() {
        ConnectionHelper.saveToken(this, spotifyToken);
        Intent intent = new Intent(this, UserMusicActivity.class);
        startActivity(intent);
        finish();
    }

    private void startGPlayServices() {
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        resolveNode();
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                        // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
    }


    @Override
    protected void onDestroy() {
        try {
            mGoogleApiClient.disconnect();
        }
        catch (Exception e) {}
        super.onDestroy();
    }

    private void sendMessage() {

        if (myNodes != null && mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            for(Node node : myNodes) {
                Wearable.MessageApi.sendMessage(
                        mGoogleApiClient, node.getId(), GET_TOKEN_PATH, spotifyToken.getBytes()).setResultCallback(

                        new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                                if (!sendMessageResult.getStatus().isSuccess()) {
                                    Log.e("TAG", "Failed to send message with status code: "
                                            + sendMessageResult.getStatus().getStatusCode());
                                }
                                else {
                                    finish();
                                }
                            }
                        }
                );
            }
        }else{
            //Improve your code
        }

    }

    private void resolveNode() {

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                myNodes = nodes.getNodes();
                Log.w(TAG, "nodes.size: " + (myNodes != null ? myNodes.size() : "null"));
                sendMessage();
            }
        });
    }
}