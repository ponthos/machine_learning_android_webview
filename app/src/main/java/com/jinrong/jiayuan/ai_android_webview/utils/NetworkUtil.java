package com.jinrong.jiayuan.ai_android_webview.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.jinrong.jiayuan.ai_android_webview.data.Constants;


/**
 * Created by anartzmugika on 22/6/16.
 */

public class   NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context,int maxSatellites) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context,int maxSatellites) {
        rxBytes= TrafficStats.getTotalRxBytes();
        seBytes=TrafficStats.getTotalTxBytes()-rxBytes;
        double downloadSpeed=(rxBytes-preRxBytes)/2;
        double uploadSpeed=(seBytes-preSeBytes)/2;
        preRxBytes=rxBytes;
        preSeBytes=seBytes;

        int conn = NetworkUtil.getConnectivityStatus(context,maxSatellites);

        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            //status = "Wifi enabled";
            status = Constants.CONNECT_TO_WIFI;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            //status = "Mobile data enabled";
            System.out.println(Constants.CONNECT_TO_MOBILE);
            status = getNetworkClass(context,maxSatellites);
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = Constants.NOT_CONNECT;
        }

        return status + " / " + DateTime.getCurrentDataTime()+"downloadSpeed:"+downloadSpeed+"uploadSpeed:"+uploadSpeed+"maxSatellites:"+maxSatellites;
    }

    public static String getNetworkClass(Context context,int maxSatellites) {
        //获取上下行流量,判断当前网络状态
        rxBytes= TrafficStats.getTotalRxBytes();
        seBytes=TrafficStats.getTotalTxBytes()-rxBytes;
        double downloadSpeed=(rxBytes-preRxBytes)/2;
        double uploadSpeed=(seBytes-preSeBytes)/2;
        preRxBytes=rxBytes;
        preSeBytes=seBytes;

        //根据流量和网络情况以及当前GPS卫星数量判断是否处于网络情况不佳状态,留作以后判断
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info == null || !info.isConnected())
            return "-";//+"downloadSpeed:"+downloadSpeed+"uploadSpeed:"+uploadSpeed+"maxSatellites:"+maxSatellites; //not connected
        if(info.getType() == ConnectivityManager.TYPE_WIFI){
            //            当前WiFi不可用是否关闭WiFi
            if(maxSatellites<3&&uploadSpeed<100.0){
                Toast.makeText(context,"当前信号弱,信号恢复后,网页将自动刷新",Toast.LENGTH_LONG).show();
            }

        return "WIFI";}
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    //当前3G信号不佳
                    if(maxSatellites<3&&uploadSpeed<100.0){
                        Toast.makeText(context,"当前3G信号不佳,请链接可用WiFi后重试",Toast.LENGTH_LONG).show();
                    }
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    //当前4G信号不佳
                    if(maxSatellites<3&&uploadSpeed<100.0){
                        Toast.makeText(context,"当前4G信号不佳,请链接可用WiFi后重试",Toast.LENGTH_LONG).show();
                    }
                    return "4G";
                default:
                    //当前信号不佳
                    if(maxSatellites<3&&uploadSpeed<100.0){
                        Toast.makeText(context,"当前信号不佳,请链接可用WiFi后重试",Toast.LENGTH_LONG).show();
                    }
                    return "UNKNOWN";
            }
        }
        return "UNKNOWN";
    }
    public static long rxBytes,seBytes,preRxBytes,preSeBytes;
}
