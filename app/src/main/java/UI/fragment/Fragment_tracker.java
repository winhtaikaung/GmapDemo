package ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.googlemapdemo.R;

import helpers.Db_helper;

import listenersInterface.IGPSChangeListener;
import ui.services.GPSTracker;

/**
 * Created by winhtaikaung on 2/13/16.
 */
public class Fragment_tracker extends Fragment implements OnMapReadyCallback,IGPSChangeListener {

    FloatingActionButton btn_save;
    CoordinatorLayout coordinatorLayout;
    SupportMapFragment mapFragment;
    GPSTracker gpsTracker;





    Db_helper db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tracker,container,false);
        mapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        db=new Db_helper(getActivity());



        bindView(view);
        bindAction();
        mapFragment.getMapAsync(this);
        return view;
    }

    void bindView(View v){
        btn_save=(FloatingActionButton) v.findViewById(R.id.btn_save);
        coordinatorLayout=(CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);


    }

    void bindAction(){
        btn_save.setOnClickListener(new OnButtonClickListener());
    }



    @Override
    public void onStart() {
        super.onStart();
        gpsTracker=new GPSTracker(getActivity(),this,mapFragment.getMap());
        if(gpsTracker.canGetLocation()){


            // \n is for new line
            //Toast.makeText(getActivity(), "Your Location is - \nLat: " + gpsTracker.getLatitude() + "\nLong: " + gpsTracker.getLongitude(), Toast.LENGTH_LONG).show();
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
    public void OnUserMove(Location l, Fragment mFragment, GoogleMap map) {
        Toast.makeText(getActivity(), "My Location is - \nLat: " + l.getLatitude() + "\nLong: " + l.getLongitude(), Toast.LENGTH_LONG).show();
        LatLng movelatlng=new LatLng(l.getLatitude(),l.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(movelatlng));
    }

    protected class OnButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_save:
                    db.addDestination(gpsTracker.getLongitude()+"|"+gpsTracker.getLatitude());

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Your Location Has Been Saved", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    break;
            }
        }
    }





}
