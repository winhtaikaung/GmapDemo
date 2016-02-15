package ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.googlemapdemo.R;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;
import com.nobrain.android.permissions.Result;

import helpers.ConnectionHelper;
import helpers.GooglePlayHelper;
import ui.fragment.Fragment_tabpager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoCondensed-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        if(!ConnectionHelper.isOnline(this)){
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setMessage("Cannot access internet,Switch on data?");
            dialog.setNeutralButton("Mobile-Data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_APN_SETTINGS));
                    dialogInterface.dismiss();
                    if(savedInstanceState==null){
                        getSupportFragmentManager().beginTransaction().add(R.id.container, new Fragment_tabpager()).commit();
                    }
                }
            });
            dialog.setNegativeButton("WI-FI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialogInterface.dismiss();
                    if(savedInstanceState==null){
                        getSupportFragmentManager().beginTransaction().add(R.id.container, new Fragment_tabpager()).commit();
                    }
                }
            });
            dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.show();
        }else{
            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction().add(R.id.container, new Fragment_tabpager()).commit();
            }
        }


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

