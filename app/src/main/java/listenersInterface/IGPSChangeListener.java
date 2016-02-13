package listenersInterface;

import android.location.Location;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by winhtaikaung on 2/14/16.
 */
public interface IGPSChangeListener {
    /***
     *
     * @param l Location
     * @param mFragment Fragment
     * @param map Google map
     */
    void OnUserMove(Location l, Fragment mFragment, GoogleMap map);
}
