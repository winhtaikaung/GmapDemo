package helpers;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by winhtaikaung on 2/13/16.
 */
public class Dbhelper {
    Context mContext;
    public Dbhelper(Context c){
        this.mContext=c;

    }

    /**
     * <p>Returns Arrays of Visited Lat Lon that user saved</p>
     * */
    public  String[] getAllDestination(){
        String[] arr=new String[0];
        String destination=PreferenceHelper.getInstance(mContext).getStringPreference(PreferenceHelper.LOCATION,"");
        if(destination.equals("")){
            arr=new String[0];
        }

        if(!destination.equals("")){
            arr= destination.split(",");
        }

        return arr;



    }
    public  String getLastDestination(){
        String destination=PreferenceHelper.getInstance(mContext).getStringPreference(PreferenceHelper.LOCATION,"");
        if(destination.equals("")){
            return "";
        }else{
            String[] last= destination.split(",");
            return last[last.length-1];
        }



    }

    public  void addDestination(String location){
        // add destination to string array and set it to preference
        String db="";
        String destination=PreferenceHelper.getInstance(mContext).getStringPreference(PreferenceHelper.LOCATION,"");
        //Lat Long
        Log.e("DEST","");
        if(destination.equals("")){
            db=location+",";
        }else{
            String[] sList=destination.split(",");

            for(String s:sList){
                db+=s+",";
            }
            db+=location+",";



        }
        PreferenceHelper.getInstance(mContext).setStringPreference(PreferenceHelper.LOCATION,db);



    }
}
