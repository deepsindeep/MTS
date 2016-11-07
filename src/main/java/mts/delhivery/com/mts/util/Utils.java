package mts.delhivery.com.mts.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import mts.delhivery.com.mts.config.Config;


/**
 * this contains some utility adhoc methods, like gps availability, internet
 * availability
 */
public class Utils {


    private static final String TAG = Config.logger + Utils.class.getSimpleName();
    private static HashMap<String, Typeface> typefaces = new HashMap<String, Typeface>();

    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.equals("null");

    }

    public static Boolean isConnectionAvailable(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static Typeface getTypeface(Context ctx, String typefaceName) {
        if (!typefaces.containsKey(typefaceName)) {
            Typeface tempTypeface = Typeface
                    .createFromAsset(ctx.getAssets(), "fonts/" + typefaceName + ".ttf");

            typefaces.put(typefaceName, tempTypeface);
        }
        return typefaces.get(typefaceName);

    }


    public static String getTime(String time) {
        try {
            String[] split = time.split(":");
            int hour = Integer.parseInt(split[0]);
            String sig = "am";

            if (hour > 11) {
                sig = "pm";
                hour = hour % 12;
            }
            return hour + ":" + split[1] + " " + sig;
        } catch (Exception e) {
            Tracer.er(TAG, "[getTime] _ " + "");
        }
        return time;
    }

    public static String gettimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());

        String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
    }

    public static String getCurrentTime() {
        ISO8601DateFormat df = new ISO8601DateFormat();
        return df.format(new Date());
    }


    public static Date getDate(String time) {
        ISO8601DateFormat iso8601DateFormat= new ISO8601DateFormat();
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
        Date date = null;
        try {
            date = iso8601DateFormat.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
//            Tracer.debug(TAG, " getDate " + "Exception " + e);
        }
        return date;
    }


    public static File getHomePath() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Fe-App/");
        return file;
    }

    public static File getSignaturePath() {
        return new File(getHomePath(), "signature");

    }


    public static File getApkPath() {
        File file = new File(getHomePath(), "apk/Fe-App.apk");
        file.setReadable(true, false);
        file.setExecutable(true, false);
        file.setWritable(true, false);
        return file;
    }

    public static File getConfigPath() {
        File apkfile = new File(getHomePath(), "config");
        if (!apkfile.exists()) {
            apkfile.mkdirs();
        }
        File file = new File(apkfile, "config.txt");

        file.setReadable(true, false);
        file.setExecutable(true, false);
        file.setWritable(true, false);
        return file;
    }




    public static String getDateTime(Long time) {
        if (time == null || time == 0)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time * 1000);

            return simpleDateFormat.format(cal.getTime());
        } catch (Exception ex) {
            System.out.println("Exception " + ex);
        }
        return null;
    }


    public static Float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return null;
        }
        return ((float) level / (float) scale) * 100.0f;
    }

    public static String getVersionName(Context context) {

        PackageInfo pInfo = null;
        try {
            pInfo = context
                    .getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "default";
    }

}
