package org.liyou.qixiaobo.entities.request;

import org.liyou.qixiaobo.utils.MessageUtil;

/**
 * Created by Administrator on 14-3-2.
 */
public class LocationEvent extends  PushEvent {
    public LocationEvent () {
        this.setEvent (MessageUtil.EVENT_TYPE_LOCATION);
    }

    private float Latitude;
    private float Longitude;
    private float Precision;

    public float getLatitude () {
        return Latitude;
    }

    public void setLatitude (float latitude) {
        Latitude = latitude;
    }

    public float getLongitude () {
        return Longitude;
    }

    public void setLongitude (float longitude) {
        Longitude = longitude;
    }

    public float getPrecision () {
        return Precision;
    }

    public void setPrecision (float precision) {
        Precision = precision;
    }

    @Override
    public String toString () {
        return "LocationEvent{" +
                "Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", Precision=" + Precision +
                "} " + super.toString ();
    }
}
