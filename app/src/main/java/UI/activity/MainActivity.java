package ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.googlemapdemo.R;

import java.util.Arrays;

import ui.fragment.Fragment_tracker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new Fragment_tracker()).commit();
        }

    }
}
