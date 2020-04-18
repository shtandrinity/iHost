package com.example.recviewfragment.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.R;

import java.util.List;

public class RVAdapter_listLogged extends RecyclerView.Adapter<RVAdapter_listLogged.MyViewHolder> {

    private Context context;
    private List<ItemArtist> mData;

    public RVAdapter_listLogged(Context context, List<ItemArtist> mData) {
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
            tvArtistName = (TextView) itemView.findViewById(R.id.item_artist_lgd);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_artist_logged, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvArtistName.setText(String.valueOf(mData.get(position).getId()) + " " +
                mData.get(position).getName());
    }
}
