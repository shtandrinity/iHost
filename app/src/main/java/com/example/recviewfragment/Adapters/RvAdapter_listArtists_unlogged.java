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
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.R;

import java.util.List;

public class RvAdapter_listArtists_unlogged extends RecyclerView.Adapter<RvAdapter_listArtists_unlogged.MyViewHolder>{
    private Context context;
    private List<ItemArtist> mData;
    private OnItemClickListener mListener;

    public RvAdapter_listArtists_unlogged(Context context, List<ItemArtist> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvArtistName;
        private TextView tvArtistID;

        MyViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            tvArtistName = (TextView) itemView.findViewById(R.id.item_artist_unlogged);
            tvArtistID = (TextView) itemView.findViewById(R.id.item_artistID_unlogged);
        }
    }

    @NonNull
    @Override
    public RvAdapter_listArtists_unlogged.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_artist_in_list_artists_unlogged, parent, false);
        return new RvAdapter_listArtists_unlogged.MyViewHolder(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RvAdapter_listArtists_unlogged.MyViewHolder holder, int position) {
        holder.tvArtistName.setText(String.valueOf(mData.get(position).getName()));
        holder.tvArtistID.setText(String.valueOf(position+1));
    }
}
