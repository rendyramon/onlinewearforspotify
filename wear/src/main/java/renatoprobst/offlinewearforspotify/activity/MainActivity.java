package renatoprobst.offlinewearforspotify.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageApi;

import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.activity.generic.GenericSingleActivity;
import renatoprobst.wearsender.OkWear;
import renatoprobst.wearsender.listener.SendResultListener;

public class MainActivity extends GenericSingleActivity<String> {

    private static final String GET_TOKEN_PATH = "/get-token-path-mobile";
    OkWear okWear;

    public MainActivity() {
        layout = R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAmbientEnabled();
        okWear = new OkWear(this);
    }


    private void sendMessage() {
            okWear.sendMessageAll(null, GET_TOKEN_PATH, new SendResultListener<MessageApi.SendMessageResult>() {
                @Override
                public void onResult(MessageApi.SendMessageResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.e("TAG", "Failed to send message with status code: "
                                + result.getStatus().getStatusCode());
                    }
                    else {
                        finish();
                    }
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        okWear.connect();

        sendMessage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        okWear.disconnect();
    }

    @Override
    public void getData() {

    }

    @Override
    public void setData() {

    }
}
