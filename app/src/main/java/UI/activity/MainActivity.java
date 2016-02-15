package ui.activity;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.googlemapdemo.R;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;
import com.nobrain.android.permissions.Result;

import helpers.GooglePlayHelper;
import ui.fragment.Fragment_tabpager;

public class MainActivity extends AppCompatActivity {


    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new Fragment_tabpager()).commit();
        }

    }
}

