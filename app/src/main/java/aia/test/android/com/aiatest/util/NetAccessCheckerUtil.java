package aia.test.android.com.aiatest.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetAccessCheckerUtil {

    private NetAccessCheckerUtil() {
    }

    public static boolean isOnline(Context context) {//Activity

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
