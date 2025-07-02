package com.adam.app.mydeviceinfo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public final class Util {
    // prevent to new instance
    private Util() {
    }

    // TAG: MyDeviceInfo
    public static final String TAG = "MyDeviceInfo";

    /**
     * log: print log
     * @param msg: String: log message
     */
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    /**
     * Show toast: show toast
     * @param context: Context: context
     * @param msg: String: toast message
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}
