package com.example.recviewfragment.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.Interfaces.OnItemClickListener;
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.R;

import java.util.List;

public class RvAdapter_listArtists_logged extends RecyclerView.Adapter<RvAdapter_listArtists_logged.MyViewHolder> {

    private Context context;
    private List<ItemArtist> mData;
    private OnItemClickListener mListener;


    public RvAdapter_listArtists_logged(Context context, List<ItemArtist> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvArtistName;
        private TextView tvArtistID;
        private ImageView ivDelete;
        private RelativeLayout rlContainer;

        @SuppressLint("ResourceAsColor")
        MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvArtistName = (TextView) itemView.findViewById(R.id.item_artist_lgd);
            tvArtistID = (TextView) itemView.findViewById(R.id.item_artistID_lgd);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete_lgd);
            rlContainer = (RelativeLayout) itemView.findViewById(R.id.list_artists_logged_container);

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
        v = LayoutInflater.from(context).inflate(R.layout.item_artist_in_list_artists_logged, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.tvArtistName.setText(String.valueOf(mData.get(position).getName()));
        holder.tvArtistID.setText(String.valueOf(position+1));
        if(mData.get(position).getIsCurrentlyOnStage()){
            holder.rlContainer.setBackgroundColor(Color.parseColor("#5bbce4"));
//            if ((position>0) && !mData.get(position - 1).getIsCurrentlyOnStage()) {
//                    holder.rlContainer.setBackgroundColor(R.color.colorPrimaryDark);
//                    holder.rlContainer.setBackgroundResource(R.drawable.rect);
//            }
        }
        else{
            holder.rlContainer.setBackgroundResource(R.drawable.rect);
        }
    }
}
