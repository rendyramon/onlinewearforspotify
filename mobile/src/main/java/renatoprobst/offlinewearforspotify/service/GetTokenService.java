package renatoprobst.offlinewearforspotify.service;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import renatoprobst.offlinewearforspotify.MainActivity;

public class GetTokenService extends WearableListenerService {

    private static final String GET_TOKEN_PATH = "/get-token-path-mobile";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        /*
         * Receive the message from wear
         */
        if (messageEvent.getPath().equals(GET_TOKEN_PATH)) {

            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }

    }

    private void createAuthSystem() {

    }

}