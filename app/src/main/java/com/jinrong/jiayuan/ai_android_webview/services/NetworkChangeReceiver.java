package com.jinrong.jiayuan.ai_android_webview.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jinrong.jiayuan.ai_android_webview.utils.NetworkUtil;


/***********************************************
 * Created by anartzmugika on 22/6/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String status = NetworkUtil.getConnectivityStatusString(context,0);

        Log.e("Receiver ", "" + status);
    }
}
