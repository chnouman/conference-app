package com.github.lecho.mobilization.ui.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;

/**
 * Created by Leszek on 2015-09-26.
 */
public class LoaderChangeObserver extends BroadcastReceiver {

    private static final String TAG = LoaderChangeObserver.class.getSimpleName();
    private static final String CONTENT_CHANGE_BROADCAST_ACTION = "content-change-broadcast-action";
    private BaseRealmLoader loader;

    public LoaderChangeObserver(BaseRealmLoader loader) {
        this.loader = loader;
    }

    public static void emitBroadcast(Context context) {
        Intent broadcastIntent = new Intent(CONTENT_CHANGE_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }

    public void register(Context context) {
        IntentFilter intentFilter = new IntentFilter(CONTENT_CHANGE_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(this, intentFilter);
    }

    public void unregister(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Received content change broadcast");
        }
        loader.onContentChanged();
    }
}
