package ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googlemapdemo.R;

import adapters.HistoryListAdapter;
import helpers.Dbhelper;

/**
 * Created by winhtaikaung on 2/14/16.
 */
public class Fragment_history extends Fragment {
    RecyclerView mHistoryList;
    LinearLayoutManager layoutManager;
    HistoryListAdapter historyListAdapter;
    Dbhelper db;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_history,container,false);

        db=new Dbhelper(getActivity());
        if(db.getAllDestination().length!=0){
            bindView(v);
        }


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("history");
        LocalBroadcastManager.getInstance(getActivity()).
                registerReceiver(receiver, intentFilter);
    }

    void bindView(View v){
        mHistoryList=(RecyclerView) v.findViewById(R.id.history_list);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHistoryList.setLayoutManager(layoutManager);

        //Getting data from SHared Preference



        historyListAdapter = new HistoryListAdapter(db.getAllDestination(),getActivity());
        mHistoryList.setAdapter(historyListAdapter);
        historyListAdapter.notifyDataSetChanged();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("history")) {
                if(db.getAllDestination().length!=0) {
                    historyListAdapter = new HistoryListAdapter(db.getAllDestination(), getActivity());
                    mHistoryList=(RecyclerView)v.findViewById(R.id.history_list) ;
                    layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mHistoryList.setLayoutManager(layoutManager);

                    mHistoryList.setAdapter(historyListAdapter);
                    historyListAdapter.notifyDataSetChanged();
                }
            }
        }
    };
}
