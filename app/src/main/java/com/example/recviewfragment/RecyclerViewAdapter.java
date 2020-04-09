package com.example.recviewfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<ItemArtist> mData;

    RecyclerViewAdapter(Context context, List<ItemArtist> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvArtistName;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArtistName = (TextView) itemView.findViewById(R.id.itemArtist);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvArtistName.setText(String.valueOf(mData.get(position).getId()) + " " +
                mData.get(position).getName());
    }
}
