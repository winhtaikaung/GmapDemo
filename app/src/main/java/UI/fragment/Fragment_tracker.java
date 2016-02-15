package ui.fragment;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.googlemapdemo.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import helpers.Dbhelper;
import helpers.GooglePlayHelper;
import listenersInterface.IGPSChangeListener;
import ui.services.GPSTracker;

/**
 * Created by winhtaikaung on 2/13/16.
 */
public class Fragment_tracker extends Fragment implements OnMapReadyCallback, IGPSChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    FloatingActionButton btn_save;
    CoordinatorLayout coordinatorLayout;
    Location mLastLocation;
    SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;


    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    GPSTracker gpsTracker;
    Dbhelper db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        db = new Dbhelper(getActivity());



        if (!GooglePlayHelper.isGPSEnabled(getActivity())) {
            GooglePlayHelper.buildAlertMessageNoGps(getActivity());
        }


        bindView(view);
        bindAction();


        if (GooglePlayHelper.isPlayServiceAvailable(getActivity())) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        } else {

        }


        mapFragment.getMapAsync(this);
        return view;
    }

    void bindView(View v) {
        btn_save = (FloatingActionButton) v.findViewById(R.id.btn_save);
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);

        btn_save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));


    }

    void bindAction() {
        btn_save.setOnClickListener(new OnButtonClickListener());
    }


    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        gpsTracker = new GPSTracker(getActivity(), this
                , googleMap);

        if (GooglePlayHelper.isPlayServiceAvailable(getActivity())) {

            displayLocationbyPlayservice();

        } else {


            if (gpsTracker.canGetLocation()) {
                LatLng location = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                googleMap.setMyLocationEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                //} else {

            } else {
                //if GPS couldn't load last known location it will load from Playservice
                //LocationServices.FusedLocationApi.getLastLocation()
                //gpsTracker.showSettingsAlert();

            }
        }
    }

    boolean checkduplicate(String latlon){
        String[] arr_destination=db.getAllDestination();
        List list=Arrays.asList(arr_destination);
        int index=Collections.binarySearch(list,latlon);
        if(index==-1){
            return false;
        }else{
            AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
            dialog.setMessage("Duplicate Location!");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
            return true;
        }

    }

    void displayLocationbyPlayservice() {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();


            LatLng location = new LatLng(latitude, longitude);

            mapFragment.getMap().setMyLocationEnabled(true);
            mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

        } else {
            if (gpsTracker.canGetLocation()) {
                LatLng location = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                mapFragment.getMap().setMyLocationEnabled(true);
                mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                //} else {

            } else {
                //if GPS couldn't load last known location it will load from Playservice
                //LocationServices.FusedLocationApi.getLastLocation()
                //gpsTracker.showSettingsAlert();

            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();


    }


    @Override
    public void onResume() {
        super.onResume();

        GooglePlayHelper.isPlayServiceAvailable(getActivity());
    }

    @Override
    public void OnUserMove(Location l, Fragment mFragment, GoogleMap map) {
        //Toast.makeText(getActivity(), "My Location is - \nLat: " + l.getLatitude() + "\nLong: " + l.getLongitude(), Toast.LENGTH_LONG).show();
        LatLng movelatlng = new LatLng(l.getLatitude(), l.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(movelatlng));
    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocationbyPlayservice();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("CONENCTION_FAILED", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected class OnButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_save:

                    mLastLocation = LocationServices.FusedLocationApi
                            .getLastLocation(mGoogleApiClient);

                    if (mLastLocation != null) {
                        //Get Location From Google Play Service
                        double latitude = mLastLocation.getLatitude();
                        double longitude = mLastLocation.getLongitude();
                        if(!checkduplicate(latitude + ":" + longitude)) {
                            db.addDestination(latitude + ":" + longitude);
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Your Location Has Been Saved", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                    }else{
                        //Fall Back situation
                        if(gpsTracker.getLocation()!=null){
                            double latitude = gpsTracker.getLatitude();
                            double longitude = gpsTracker.getLongitude();
                            if(!checkduplicate(latitude + ":" + longitude)) {
                                db.addDestination(latitude + ":" + longitude);
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Your Location Has Been Saved", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                        }
                    }




                    break;
            }
        }
    }


}
