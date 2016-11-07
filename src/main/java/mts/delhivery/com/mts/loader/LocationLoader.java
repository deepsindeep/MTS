package mts.delhivery.com.mts.loader;

import mts.delhivery.com.mts.MTS;
import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.database.Location;
import mts.delhivery.com.mts.domain.BaseResponse;
import mts.delhivery.com.mts.domain.ErrorD;
import mts.delhivery.com.mts.listener.BaseListener;
import mts.delhivery.com.mts.listener.LocationUpdateListener;
import mts.delhivery.com.mts.net.UrlResolver;
import mts.delhivery.com.mts.util.JsonUtil;
import mts.delhivery.com.mts.util.Tracer;

/**
 * Created by delhivery on 27/4/16.
 */
public class LocationLoader extends BaseLoader {
    private final String TAG = Config.logger + LocationLoader.class.getSimpleName();

    public void updateLocation(Location location, LocationUpdateListener listener) {
        Tracer.debug(TAG," updateLocation "+" ");
        String url = UrlResolver.getPath(UrlResolver.EndPoints.LOCATION_UPDATE);
        String json = JsonUtil.jsonify(location);
        String token = MTS.getinstance().getToken();
        requestPost(url, json, token, listener);
    }


    @Override
    protected void onSuccess(String json, BaseListener baseListner) {
        Tracer.debug(TAG, " onSuccess " + json);
        BaseResponse baseResponse = (BaseResponse) JsonUtil.objectify(json, BaseResponse.class);
        if (baseResponse != null)
            ((LocationUpdateListener) baseListner).onLocationUpdate(true, null);
        else
            ((LocationUpdateListener) baseListner).onLocationUpdate(false, ErrorD.getJsonrError());

    }

    @Override
    protected void onFailure(ErrorD error, BaseListener baseListner, String json) {
        Tracer.debug(TAG, " onFailure " + json);
        ((LocationUpdateListener) baseListner).onLocationUpdate(false, error);
    }
}
