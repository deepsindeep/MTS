package mts.delhivery.com.mts.listener;


import mts.delhivery.com.mts.domain.ErrorD;

/**
 * Created by delhivery on 27/4/16.
 */
public interface LocationUpdateListener extends BaseListener {

    public void onLocationUpdate(boolean success, ErrorD error);
}
