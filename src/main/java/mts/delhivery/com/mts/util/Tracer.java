package mts.delhivery.com.mts.util;

import android.util.Log;

import com.logentries.logger.AndroidLogger;

import mts.delhivery.com.mts.config.Config;


public class Tracer {

    private final static Boolean LOG_ENABLE = true;
    private static final String TAG_ = Config.logger + Tracer.class.getSimpleName();


    public static void debug(String TAG, String message) {
        if (LOG_ENABLE) {
            Log.d(TAG, message);
        }
    }


    public static void reportOnLogenteries(String TAG, String message) {
        Log.i(TAG+"_ROL",message);
        try {
                AndroidLogger.getInstance().log(TAG + " - " + message);

        } catch (Exception e) {
            debug(TAG_,e.getMessage());
        }

    }

    public static void error(String TAG, String message) {
        if (LOG_ENABLE) {
            Log.e(TAG, message);
        }
    }

    public static void d(String TAG, String message) {
        if (LOG_ENABLE) {
            Log.d(TAG, message);
        }
    }

    public static void imp(String TAG, String message) {
        if (LOG_ENABLE) {
            Log.i(TAG + "_imp", message);
        }
    }

    public static void in(String TAG, String message) {

        Log.i(TAG + "_info", message);

    }

    public static void er(String TAG, String message) {
        Log.e(TAG + "_error", message);
    }

}
