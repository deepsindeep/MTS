package mts.delhivery.com.mts;

import android.content.Context;
import android.content.Intent;

import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.service.MTSService;
import mts.delhivery.com.mts.util.Tracer;

/**
 * Created by root on 27/10/16.
 */

public class MTS {

    public enum EnvironmentName{
        STG, DEV, PROD;
    }

    private final String TAG = Config.logger + MTS.class.getSimpleName();
    private static MTS mts;
    private static String mtoken;
    private static Context mcontext;
    private static boolean mGeoFaceAPIEnable;
    private static int mlocationFrequency;
    private static String mdeviceId;
    private static EnvironmentName mEnvironment;


    private  MTS() {
        if (mtoken == null || mcontext == null)
            new Exception("Please init Mts before using it ");
    }

    public static MTS getinstance() {
        if (mts == null) {
            mts = new MTS();
        }
        return mts;
    }

    public static void initMTS(Context appContext, String token, Boolean geoFacingAPI, int locationFrequecy, String deviceid, EnvironmentName environmentName) {
        mcontext = appContext;
        mtoken = token;
        mdeviceId = deviceid;
        mGeoFaceAPIEnable = geoFacingAPI;
        mlocationFrequency = locationFrequecy;
        mEnvironment = environmentName;
        getinstance();
    }

    public void startMTS(){
        Tracer.debug(TAG," stopMTS "+" ");
        Intent location = new Intent(mcontext, MTSService.class);
        mcontext.startService(location);
    }

    public void stopMTS(){
        Tracer.debug(TAG," stopMTS "+" ");
        Intent intent = new Intent(mcontext, MTSService.class);
        mcontext.stopService(intent);
    }

    public int getGeoFacingAPI() {
        return mlocationFrequency;
    }
    public String getToken() {
        return mtoken;
    }
    public Context getContext() {
        return mcontext;
    }
    public String getDeviceId(){return mdeviceId;}
    public boolean isGeofacingRequired() {
        return mGeoFaceAPIEnable;
    }
    public EnvironmentName getEnviornment(){return mEnvironment; }
}
