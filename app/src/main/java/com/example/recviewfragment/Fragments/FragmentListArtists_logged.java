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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.Interfaces.CallbackInterfaceArtistsList;
import com.example.recviewfragment.Interfaces.FragmentListInterface;
import com.example.recviewfragment.Interfaces.OnItemClickListener;
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.PreferenceUtils;
import com.example.recviewfragment.R;
import com.example.recviewfragment.Adapters.RvAdapter_listArtists_logged;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListArtists_logged extends Fragment implements FragmentListInterface {

    private View v;
    private JsonPlaceHolder jsonPlaceHolder;
    private List<ItemArtist> lstItemArtists = new ArrayList<>();
    private RvAdapter_listArtists_logged recyclerAdapter;
    private TextView tvEventNameLogged;
    private PreferenceUtils preferenceUtils = null;
    private String eventName = "";
    private final String FRAGMENT_TAG = "listArtistsLogged_screen";

    public FragmentListArtists_logged(){}

    public FragmentListArtists_logged newInstance (){
        return new FragmentListArtists_logged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonPlaceHolder = ApiClient.getInterface();
        preferenceUtils = new PreferenceUtils(getContext());
        eventName = preferenceUtils.getString("eventName_to_ListOfArtists");

        getList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list_artists_logged, container, false);

        setRecyclerView();

        return v;
    }

    public void setRecyclerView(){
        tvEventNameLogged = (TextView) v.findViewById(R.id.tvListArtists_eventName);
        RecyclerView myRecyclerView = (RecyclerView) v.findViewById(R.id.listArtists_recyclerView);
        recyclerAdapter = new RvAdapter_listArtists_logged(getContext(), lstItemArtists);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        myRecyclerView.setAdapter(recyclerAdapter);

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
    }

    public void getList(){
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

    @Override
    public void onResume() {
        if(!(preferenceUtils.getBoolean("isLogged"))){
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();
            trans.replace(R.id.listArtistsLogged_container, new FragmentListArtists_unlogged(), "lArtistsLogged-lArtistsUnlogged");
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(FRAGMENT_TAG);
            trans.commit();

            FragmentManager fm = getActivity().getSupportFragmentManager();
            for (Fragment frag : fm.getFragments()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                }
                else childFm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        super.onResume();
    }
}

