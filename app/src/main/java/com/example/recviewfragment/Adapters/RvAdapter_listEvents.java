package com.example.recviewfragment.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.Interfaces.OnItemClickListener;
import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.R;

import java.util.List;

//OnItemClick listener -- https://www.youtube.com/watch?v=bhhs4bwYyhc

public class RvAdapter_listEvents extends RecyclerView.Adapter<RvAdapter_listEvents.MyViewHolder>{
    private Context context;
    private List<ItemHost> mData;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RvAdapter_listEvents(Context context, List<ItemHost> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvEventName;
        private TextView tvEventId;

        MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvEventName = (TextView) itemView.findViewById(R.id.item_eventName_unlogged);
            tvEventId = (TextView) itemView.findViewById(R.id.item_eventID_unlogged);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){                                   //making sure the position is valid
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_event_list_events, parent, false);
        return new RvAdapter_listEvents.MyViewHolder(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvEventName.setText(String.valueOf(mData.get(position).getEventName()));
        holder.tvEventId.setText(String.valueOf(position+1));
    }
}
