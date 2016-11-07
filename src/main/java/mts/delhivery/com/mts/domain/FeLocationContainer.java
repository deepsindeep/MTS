package mts.delhivery.com.mts.domain;

/**
 * Created by delhivery on 1/6/16.
 */
public class FeLocationContainer {

    public static volatile FeLocationContainer mContainer;
    private Double latitude;
    private Double longitude;

    private FeLocationContainer() {
    }


    public static FeLocationContainer getContainer() {
        if (mContainer == null)
            mContainer = new FeLocationContainer();
        return mContainer;
    }

    public boolean hasLocation() {
        return latitude != null;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
