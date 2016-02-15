package adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.googlemapdemo.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by winhtaikaung on 2/14/16.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {

    String[] mArrString;
    Context mContext;
    Geocoder mGeocoder;

    public HistoryListAdapter(String[] arr_string, Context c, Geocoder geocoder){
        this.mContext=c;
        this.mArrString=arr_string;
        this.mGeocoder=geocoder;

    }


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.history_item, parent, false);

        return new HistoryViewHolder(itemView);
    }

   public void addlocation(String location){
        this.mArrString[mArrString.length+1]=location;
        notifyDataSetChanged();
    }

    public void swaplist(String[] arr){
        this.mArrString=arr;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        String[] lat_lon=mArrString[position].split(":");

        List<Address> addresses;
        try {
            addresses = mGeocoder.getFromLocation(Double.parseDouble(lat_lon[0]), Double.parseDouble(lat_lon[1]), 1);
            String address = (addresses.get(0).getAddressLine(0)==null)?"none":addresses.get(0).getAddressLine(0)+",";

            String state = (addresses.get(0).getAdminArea()==null)?"":addresses.get(0).getAdminArea()+",";
            String country = (addresses.get(0).getCountryName()==null)?"none":addresses.get(0).getCountryName()+"";
            String postalCode = (addresses.get(0).getPostalCode()==null)?"none":addresses.get(0).getPostalCode();
            // Only if available else return NULL

            holder.tv_lat.setText(address+state+country);
            holder.tv_lon.setText((addresses.get(0).getPostalCode()==null)?"none":addresses.get(0).getPostalCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("LAT_LON",lat_lon[0]);
        Log.e("LAT_LON",lat_lon[1]);

        //holder.tv_lat.setText(lat_lon[0]);

    }

    @Override
    public int getItemCount() {
        return mArrString.length;
    }


    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView tv_lat;
        protected TextView tv_lon;


        protected CardView list_item_layout;

        public HistoryViewHolder(View itemView){
            super(itemView);
            list_item_layout=(CardView) itemView.findViewById(R.id.list_item_layout);
            tv_lat=(TextView) itemView.findViewById(R.id.tv_lat);
            tv_lon=(TextView) itemView.findViewById(R.id.tv_lon);

        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

        }
    }
}


