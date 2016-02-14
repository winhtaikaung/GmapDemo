package helpers;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by winhtaikaung on 2/14/16.
 */
public class GooglePlayHelper  {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static boolean isPlayServiceAvailable(Context c) {

        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(c);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode,c,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(c,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();

            }
            return false;
        }
        return true;
    }
}
