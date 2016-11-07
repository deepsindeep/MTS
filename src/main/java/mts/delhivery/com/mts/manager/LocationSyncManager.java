package mts.delhivery.com.mts.manager;

import android.content.Context;

import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.database.Location;
import mts.delhivery.com.mts.domain.ErrorD;
import mts.delhivery.com.mts.listener.LocationUpdateListener;
import mts.delhivery.com.mts.loader.LocationLoader;
import mts.delhivery.com.mts.util.Tracer;
import mts.delhivery.com.mts.util.Utils;

/**
 * Created by delhivery on 13/5/16.
 */
public class LocationSyncManager {

    private final String TAG = Config.logger + LocationSyncManager.class.getSimpleName();
    private LocationLoader mLoader;
    private static volatile LocationSyncManager sManager;
    private boolean inProgress;
    private Location mLocation;
    private boolean connectivityCheck = false;
    private static int count = 0;
    private static Context mContext;

    /*
    * Single ton class to sysnc all signature to s3 storage
    * */
    public static LocationSyncManager getInstance(Context context) {
        mContext = context;
        if (sManager == null) {
            sManager = new LocationSyncManager();
        }
        return sManager;
    }

    private LocationSyncManager() {
        mLoader = new LocationLoader();
    }

    /*
    * Check if all Location already synced or not
    * */
    public boolean isSyncPending() {
        return DBManager.getLocationsCount() > 0;
    }

    /*
    * Start syscing all signature file serially
    * */
    public void startSync() {
        Tracer.debug(TAG, " startSync " + " size =" + DBManager.getLocationsCount());
       /* if (!inProgress) {
            inProgress = true;*/
            syncLocation();
       // }
    }

    /*
    * Sysc location on the top of list
    * */
    private void syncLocation() {
        Tracer.debug(TAG, " syncLocation " + " ");
        if (DBManager.getLocationsCount() > 0 && Utils.isConnectionAvailable(mContext)) {
            mLocation = DBManager.getLocation();
            mLoader.updateLocation(mLocation, locationUpdateListener);
        } else {
            inProgress = false;
        }

    }

    /*
    * Listener to handle callback when location  is succesfully synced or failed
    * */

    private LocationUpdateListener locationUpdateListener = new LocationUpdateListener() {
        @Override
        public void onLocationUpdate(boolean success, ErrorD error) {
            Tracer.debug(TAG, " onLocationUpdate " + " count =" + count);
            if (success) {
                DBManager.removeLocation(mLocation);
            }
            syncLocation();
        }
    };


}
