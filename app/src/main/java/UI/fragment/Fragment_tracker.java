package ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;
import com.nobrain.android.permissions.Result;

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

    public static final int REQUEST_CODE = 102;
    private static final String TAG = "";


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
            buildAlertMessageNoGps(getActivity());
        }
        // Asking for Permissions to grab Location
        AndroidPermissions.check(getActivity())
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        Log.d(TAG, msg);
                        /*Toast.makeText(getActivity(),
                                msg,
                                Toast.LENGTH_SHORT).show();*/



                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];
                        Log.d(TAG, msg);
                        /*Toast.makeText(getActivity(),
                                msg,
                                Toast.LENGTH_SHORT).show();*/

                        ActivityCompat.requestPermissions(getActivity()
                                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , REQUEST_CODE);
                    }
                })
                .check();



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

    @Override
    public void onRequestPermissionsResult(int requestCode, final @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndroidPermissions.result(getActivity())
                .addPermissions(REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION)
                .putActions(REQUEST_CODE, new Result.Action0() {
                    @Override
                    public void call() {
                        String msg = "Request Success : " + permissions[0];
                        /*Toast.makeText(getActivity(),
                                msg,
                                Toast.LENGTH_SHORT).show();*/

                            displayLocationbyPlayservice();


                    }
                }, new Result.Action1() {
                    @Override
                    public void call(String[] hasPermissions, String[] noPermissions) {
                        String msg = "Request Fail : " + noPermissions[0];
                        /*Toast.makeText(getActivity(),
                                msg,
                                Toast.LENGTH_SHORT).show();*/


                    }
                })
                .result(requestCode, permissions, grantResults);
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
        boolean duplicate=false;
        String[] arr_destination=db.getAllDestination();
        List list=Arrays.asList(arr_destination);
        int index=Collections.binarySearch(list,latlon);
        if(index==-1){
            duplicate= false;
        }else if(index>-1){

            duplicate= true;
        }
        return duplicate;

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
            gpsTracker=new GPSTracker(getActivity(),this,mapFragment.getMap());
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
        displayLocationbyPlayservice();


    }

    @Override
    public void OnUserMove(Location l, Fragment mFragment, GoogleMap map) {

        db=new Dbhelper(getActivity());
        if(!db.getLastDestination().equals("")){
            String[]last_location=db.getLastDestination().split(":");
            Location lasLocation=new Location("lastLocation");
            lasLocation.setLatitude(Double.parseDouble(last_location[0]));
            lasLocation.setLongitude(Double.parseDouble(last_location[1]));


            Log.e("Distance",String.valueOf(GooglePlayHelper.calculateDistance(lasLocation,l)));
            if(GooglePlayHelper.calculateDistance(lasLocation,l)<=1000){

                //Calculating the User location By Current Location
                if(checkduplicate(l.getLatitude()+":"+l.getLongitude())){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                    dialog.setMessage("You have been this place within 1 Km");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.show();
                }

            }


        }

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
                        }else{
                            AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                            dialog.setMessage("Duplicate Location is not allowed to Save");
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            dialog.show();
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
                            }else{
                                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                                dialog.setMessage("Duplicate Location is not allowed to Save");
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }




                    break;
            }
        }
    }
    public  void buildAlertMessageNoGps(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.msg_gps_alert))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        dialog.cancel();
                        getActivity().finish();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
