package com.example.recviewfragment.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.Interfaces.CallbackInterfaceArtistsList;
import com.example.recviewfragment.Interfaces.OnItemClickListener;
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.PreferenceUtils;
import com.example.recviewfragment.R;
import com.example.recviewfragment.Adapters.RVAdapter_listLogged;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListLogged extends Fragment{

    View v;
    private JsonPlaceHolder jsonPlaceHolder;
    private List<ItemArtist> lstItemArtists = new ArrayList<>();
    private RVAdapter_listLogged recyclerAdapter;
    private TextView tvEventNameLogged;
    private PreferenceUtils preferenceUtils = null;
    String eventName = "";

    public FragmentListLogged(){}

    public FragmentListLogged newInstance (){
        return new FragmentListLogged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list_logged, container, false);

        setRecyclerView();

        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                Integer artistIdOnAPI = lstItemArtists.get(position).getId();
                Call<Void> call = jsonPlaceHolder.deleteArtist(artistIdOnAPI);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("Artist delete status: ", "SUCCESS");
                        lstItemArtists.remove(position);
                        recyclerAdapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Artist delete status - ", "FAILURE: " + t);
                    }
                });
            }

            @Override
            public void onItemClick(int position) {}
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonPlaceHolder = ApiClient.getInterface();
        preferenceUtils = new PreferenceUtils(getContext());
        eventName = preferenceUtils.getString("eventNameToLogged");

        getList();
    }

    public void setRecyclerView(){
        tvEventNameLogged = (TextView) v.findViewById(R.id.tvListLogged_eventName);
        RecyclerView myRecyclerView = (RecyclerView) v.findViewById(R.id.listLogged_recyclerView);
        recyclerAdapter = new RVAdapter_listLogged(getContext(), lstItemArtists);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        myRecyclerView.setAdapter(recyclerAdapter);
    }

    private void getList(){
        Call<List<ItemArtist>> call = jsonPlaceHolder.getArtistsByHostID(preferenceUtils.getInteger("itemHostID"));
        call.enqueue(new Callback<List<ItemArtist>>() {

            @Override
            public void onResponse(Call<List<ItemArtist>> call, Response<List<ItemArtist>> response) {

                callbackInterfaceArtistsList.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<ItemArtist>> call, Throwable t) {
                callbackInterfaceArtistsList.onFailure(t.toString());
            }
        });
    }

    private CallbackInterfaceArtistsList callbackInterfaceArtistsList = new CallbackInterfaceArtistsList() {
        //Refresh RecyclerView after having GET call done
        @Override
        public void onSuccess(List<ItemArtist> list) {
                for (int i=0; i<list.size(); i++) {
                    lstItemArtists.add(new ItemArtist(
                            list.get(i).getId(),
                            list.get(i).getName(),
                            list.get(i).getPhone(),
                            list.get(i).getIsLocated()));
                }
            tvEventNameLogged.setText(eventName);
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(String errorCode) {
            Log.e("Error code is:", errorCode);
        }
    };
}

