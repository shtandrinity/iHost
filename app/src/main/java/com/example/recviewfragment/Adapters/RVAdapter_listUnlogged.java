package com.example.recviewfragment.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.R;

import java.util.List;

public class RVAdapter_listUnlogged extends RecyclerView.Adapter<RVAdapter_listUnlogged.MyViewHolder>{
    private Context context;
    private List<ItemHost> mData;

    public RVAdapter_listUnlogged(Context context, List<ItemHost> mData) {
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

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = (TextView) itemView.findViewById(R.id.item_eventName_unlogged);
            tvEventId = (TextView) itemView.findViewById(R.id.item_eventID_unlogged);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_event_unlogged, parent, false);
        return new RVAdapter_listUnlogged.MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvEventName.setText(String.valueOf(mData.get(position).getEventName()));
        holder.tvEventId.setText(String.valueOf(position+1));
    }
}
