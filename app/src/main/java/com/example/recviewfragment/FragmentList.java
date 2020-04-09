package com.example.recviewfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentList extends Fragment{

    View v;
    private JsonPlaceHolder jsonPlaceHolder;
    private List<ItemArtist> lstItemArtists = new ArrayList<>();
    private RecyclerViewAdapter recyclerAdapter;

    public FragmentList(){}

    public FragmentList newInstance (){
        FragmentList fragmentList = new FragmentList();
        return fragmentList;
    }

    private CallbackInterfaceArtistsList callbackInterfaceArtistsList = new CallbackInterfaceArtistsList() {
        @Override
        public void onSuccess(List<ItemArtist> list) {
            for (ItemArtist itemArtist : list) {
                lstItemArtists.add(new ItemArtist(
                        itemArtist.getId(),
                        itemArtist.getName(),
                        itemArtist.getPhone(),
                        itemArtist.getIsLocated()));
            }
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(String errorCode) {
            Log.e("Error code is:", errorCode);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView myRecyclerView = (RecyclerView) v.findViewById(R.id.list_recyclerView);
        recyclerAdapter = new RecyclerViewAdapter(getContext(), lstItemArtists);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        myRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonPlaceHolder = ApiClient.getInterface();

        getList();
    }

    private void getList(){
        Call<List<ItemArtist>> call = jsonPlaceHolder.getItemList();
        call.enqueue(new Callback<List<ItemArtist>>() {

            @Override
            public void onResponse(Call<List<ItemArtist>> call, Response<List<ItemArtist>> response) {
                if(response.isSuccessful()) {
                    callbackInterfaceArtistsList.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ItemArtist>> call, Throwable t) {
                callbackInterfaceArtistsList.onFailure(t.toString());
            }
        });
    }
}

