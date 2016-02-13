package ui.fragment;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.googlemapdemo.R;

import ui.services.GPSTracker;

/**
 * Created by winhtaikaung on 2/13/16.
 */
public class Fragment_tracker extends Fragment implements OnMapReadyCallback,LocationSource.OnLocationChangedListener {


    SupportMapFragment mapFragment;
    GPSTracker gpsTracker;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tracker,container,false);
        mapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        gpsTracker=new GPSTracker(getActivity());
        if(gpsTracker.canGetLocation()){


            // \n is for new line
            Toast.makeText(getActivity(), "Your Location is - \nLat: " + gpsTracker.getLatitude() + "\nLong: " + gpsTracker.getLongitude(), Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(gpsTracker.canGetLocation()){
            LatLng location=new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

        }else{

        }
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();
        if(gpsTracker.canGetLocation()){


            // \n is for new line
            // Toast.makeText(getActivity(), "Your Location is - \nLat: " + gpsTracker.getLatitude() + "\nLong: " + gpsTracker.getLongitude(), Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }



    @Override
    public void onLocationChanged(Location location) {

        //Toast.makeText(getActivity(), "Your Location is - \nLat: " + location.getLatitude() + "\nLong: " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }
}
