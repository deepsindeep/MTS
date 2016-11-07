package mts.delhivery.com.mts.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import mts.delhivery.com.mts.MTS;
import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.domain.FeLocationContainer;
import mts.delhivery.com.mts.manager.DBManager;
import mts.delhivery.com.mts.util.Tracer;
import mts.delhivery.com.mts.util.Utils;


/**
 * Created by delhivery on 13/9/16.
 */
public class MTSService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final String TAG = Config.logger + MTSService.class.getSimpleName();


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean isAlreadyStarted;


    @Override
    public void onCreate() {
        Tracer.reportOnLogenteries(TAG, " onCreate " + " ");
        super.onCreate();
        initLocationRequest();
        initClient();
        DBManager.init(getApplicationContext());
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MTS.getinstance().getGeoFacingAPI());
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void initClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Tracer.reportOnLogenteries(TAG, " onStartCommand " + " ");
        startUpdates();
       // Context context  = MTSService.this;
        makeForeGround();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startUpdates() {
        Tracer.debug(TAG, " startService " + " ");
        if (!isAlreadyStarted) {
            mGoogleApiClient.connect();
            isAlreadyStarted = true;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Tracer.reportOnLogenteries(TAG, " onConnected " + " ");
        isAlreadyStarted = true;
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int reson) {
        Tracer.reportOnLogenteries(TAG, " onConnectionSuspended  reason" + reson);
        isAlreadyStarted = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Tracer.reportOnLogenteries(TAG, " onConnectionFailed " + connectionResult.getErrorMessage());
        isAlreadyStarted = false;
    }

    protected void startLocationUpdates() {
        Tracer.debug(TAG, " startLocationUpdates " + " ");
        PendingResult<Status> statusPendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Tracer.debug(TAG," onLocationChanged "+" ");
        if (location != null) {
            FeLocationContainer.getContainer().setLatitude(location.getLatitude());
            FeLocationContainer.getContainer().setLongitude(location.getLongitude());
            mts.delhivery.com.mts.database.Location loc = new  mts.delhivery.com.mts.database.Location();
            loc.setLatitude(location.getLatitude());
            loc.setAccuracy(location.getAccuracy());
            loc.setLongitude(location.getLongitude());
            loc.setDevice_id(MTS.getinstance().getDeviceId());
            loc.setBattery(Utils.getBatteryLevel(getApplication().getApplicationContext()));
            loc.setTime(Utils.getCurrentTime());
            loc.setEpoc(Calendar.getInstance().getTimeInMillis());
            loc.setApplication("dispatcher");
            DBManager.putLocation(loc);
            if (MTS.getinstance().isGeofacingRequired()) {
                EventBus.getDefault().post(location);
            }
        }
    }

    private void makeForeGround() {
        Tracer.debug(TAG, " makeForeGround " + " ");

        Intent notificationIntent  = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Fe-App")
                .setContentText("Location Service")
                .setContentIntent(pendingIntent).build();
        startForeground(1337, notification);
    }

    @Override
    public void onDestroy() {
        Tracer.debug(TAG, " onDestroy " + " ");
        stopLocationUpdates();
        stopForeground(true);
        super.onDestroy();
    }

}
