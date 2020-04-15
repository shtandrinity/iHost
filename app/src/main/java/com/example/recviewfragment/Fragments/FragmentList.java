package com.example.recviewfragment.Fragments;

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

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.CallbackInterfaces.CallbackInterfaceArtistsList;
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.PreferenceUtils;
import com.example.recviewfragment.R;
import com.example.recviewfragment.Adapters.RecyclerViewAdapter;

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
        return new FragmentList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView myRecyclerView = (RecyclerView) v.findViewById(R.id.list_recyclerView);
        recyclerAdapter = new RecyclerViewAdapter(getContext(), lstItemArtists);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        myRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        //===============================================================
        //new PreferenceUtils(getContext()).clearSavedInSharedPreference();
        //===============================================================
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

    //Refresh RecyclerView after having GET call done
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
}

