package adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.googlemapdemo.R;

/**
 * Created by winhtaikaung on 2/14/16.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {

    String[] mArrString;
    Context mContext;

    public HistoryListAdapter(String[] arr_string,Context c){
        this.mContext=c;
        this.mArrString=arr_string;
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

        Log.e("LAT_LON",lat_lon[0]);
        Log.e("LAT_LON",lat_lon[1]);

        holder.tv_lat.setText(lat_lon[0]);
        holder.tv_lon.setText(lat_lon[1]);
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


