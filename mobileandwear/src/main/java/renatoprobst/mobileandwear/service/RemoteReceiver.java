package renatoprobst.mobileandwear.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Renato on 30/04/2016.
 */
public class RemoteReceiver extends BroadcastReceiver {
    private static final String TAG = "RemoteReceiver";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;



    }
}