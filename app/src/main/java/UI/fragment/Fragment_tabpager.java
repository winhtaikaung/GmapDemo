package ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.googlemapdemo.R;

import adapters.TabsPagerAdapter;

/**
 * Created by winhtaikaung on 2/14/16.
 */
public class Fragment_tabpager extends Fragment   {
    TabLayout tabLayout;
    String[] titles={"My Navigator","My history"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tabpager,container,false);
        bindView(view);
        return view;
    }

    void bindView(View view){
        tabLayout=(TabLayout)view.findViewById(R.id.tabs);
      final  ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getChildFragmentManager(),titles);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==1) {
                    Intent intent = new Intent();
                    intent.setAction("history");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

}
