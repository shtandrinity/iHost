package com.example.recviewfragment.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.Interfaces.OnItemClickListener;
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.R;

import java.util.List;

public class RVAdapter_listLogged extends RecyclerView.Adapter<RVAdapter_listLogged.MyViewHolder> {

    private Context context;
    private List<ItemArtist> mData;
    private OnItemClickListener mListener;

    public RVAdapter_listLogged(Context context, List<ItemArtist> mData) {
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
        private ImageView ivDelete;

        MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvArtistName = (TextView) itemView.findViewById(R.id.item_artist_lgd);
            tvArtistID = (TextView) itemView.findViewById(R.id.item_artistID_lgd);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete_lgd);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){                                   //making sure the position is valid
                            listener.onDeleteClick(position);
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
        v = LayoutInflater.from(context).inflate(R.layout.item_artist_logged, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvArtistName.setText(String.valueOf(mData.get(position).getName()));
        holder.tvArtistID.setText(String.valueOf(position+1));
    }
}
