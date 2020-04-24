package com.example.recviewfragment.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.Adapters.RvAdapter_listEvents;
import com.example.recviewfragment.Interfaces.CallbackInterfaceMap;
import com.example.recviewfragment.Interfaces.OnItemClickListener;
import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.PreferenceUtils;
import com.example.recviewfragment.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListEvents extends Fragment implements OnMapReadyCallback {

    private View v;
    private List<ItemHost> lstItemEvents = new ArrayList<>();
    private List<ItemHost> lstItemEventsAll = new ArrayList<>();
    private List<String> isInCity = new ArrayList<>();
    private RvAdapter_listEvents recyclerAdapter;

    private final String FRAGMENT_TAG = "listEvents_screen";
    private JsonPlaceHolder jsonPlaceHolder;

    private PreferenceUtils preferenceUtils;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap map;
    private List<Address> eventCityAddress = null;

    LocationRequest mLocationRequest;
    Location mLastLocation;


    public FragmentListEvents() {}

    public static FragmentListEvents newInstance() { return new FragmentListEvents(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_events, container, false);

        preferenceUtils = new PreferenceUtils(getContext());
        //preferenceUtils.clearSavedInSharedPreference();
        if(preferenceUtils.getBoolean("isLogged")){
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();
            trans.replace(R.id.listEvents_container, new FragmentListArtists_logged(), "HostUnlogged-HostProfile");
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(FRAGMENT_TAG);
            trans.commit();
        }

        RecyclerView myRecyclerView = (RecyclerView) v.findViewById(R.id.listEvents_recyclerView);
        recyclerAdapter = new RvAdapter_listEvents(getContext(), lstItemEvents);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        myRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                for(int i=0; i<lstItemEventsAll.size(); i++){
                    if(lstItemEventsAll.get(i).getEventName().equals(lstItemEvents.get(position).getEventName())) {

                        preferenceUtils = new PreferenceUtils(getActivity());
                        preferenceUtils.setInteger("itemHostID", lstItemEventsAll.get(i).getId());

                        int b = lstItemEventsAll.size();

                        String itemHostBody = preferenceUtils.serializeToJson(lstItemEventsAll.get(i));
                        preferenceUtils.setString("itemHostEvent", itemHostBody);
                        preferenceUtils.setString("eventName_to_ListOfArtists", lstItemEventsAll.get(i).getEventName());

                        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
                        trans.replace(R.id.listEvents_container, new FragmentListArtists_unlogged(), "lEvents-lArtists_Unlogged");
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.addToBackStack(FRAGMENT_TAG);
                        trans.commit();
                    }
                }
            }

            @Override
            public void onDeleteClick(int position) {}
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.listEvents_map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation();

        return v;
    }

    private void fetchLastLocation(){
//        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(getActivity(), new String[]                           //If app doesn't have a permissions - ask for them
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    callbackInterfaceMap.getEventsListInCity(location);
                }
            }
        });
        fusedLocationClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("GET CURRENT LOCATION ERROR", e.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private CallbackInterfaceMap callbackInterfaceMap = new CallbackInterfaceMap() {
        @Override
        public void getEventsListInCity(Location location) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            List<Address> userCityAddress = null;
            try {
                userCityAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String userCity = userCityAddress.get(0).getLocality();

            jsonPlaceHolder = ApiClient.getInterface();
            Call<List<ItemHost>> call = jsonPlaceHolder.getHostList();
            call.enqueue(new Callback<List<ItemHost>>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<List<ItemHost>> call, Response<List<ItemHost>> response) {

//                  ==If event Cityname == current user Cityname then display vent marker on map====
                    for (int i = 0; i < response.body().size(); i++) {
                        try {
                            eventCityAddress = geocoder.getFromLocation(response.body().get(i).getLatitude(),
                                    response.body().get(i).getLongitude(),
                                    1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String eventCity = eventCityAddress.get(0).getLocality();

//                      ==add user marker==========
                        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptionsUser = new MarkerOptions()
                                .position(userLatLng)
                                .icon(getMarkerIcon("#0d3f8d"))
                                .title("Me");
                        map.addMarker(markerOptionsUser);
                        builder.include(userLatLng);

                        if (eventCity.equals(userCity)) {
                            ItemHost itemHost = new ItemHost(
                                    response.body().get(i).getEmail(),
                                    response.body().get(i).getEventName(),
                                    response.body().get(i).getPassword(),
                                    response.body().get(i).getLogin(),
                                    response.body().get(i).getLatitude(),
                                    response.body().get(i).getLongitude(),
                                    response.body().get(i).getId());
                            lstItemEvents.add(itemHost);
                            lstItemEventsAll.add(itemHost);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position((new LatLng(response.body().get(i).getLatitude(), response.body().get(i).getLongitude())))
                                    .title(response.body().get(i).getEventName());
                            map.addMarker(markerOptions);
                            builder.include((new LatLng(response.body().get(i).getLatitude(), response.body().get(i).getLongitude())));

                        }
                        else{
                            lstItemEventsAll.add(new ItemHost("","","","",0,0));
                        }
                    }
                    recyclerAdapter.notifyDataSetChanged();
                    LatLngBounds bounds = builder.build();
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 25));

                    Log.d("lstItemEvents SIZE is", String.valueOf(lstItemEvents.size()));
                    Log.d("lstItemEventsAll SIZE is", String.valueOf(lstItemEventsAll.size()));
                }


                @Override
                public void onFailure(Call<List<ItemHost>> call, Throwable t) {
                    Log.e("Error code is:", t.getMessage());
                }
            });
        }
    };

//===========================================Set map icons==========================================
    private BitmapDescriptor getMarkerIcon(String color) {
        //Set color for the map marker
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        //Set image as an map marker
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onResume() {
        if(preferenceUtils.getBoolean("isLogged")){
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();
            trans.replace(R.id.listEvents_container, new FragmentListArtists_logged(), "ListUnlogged-ListLogged");
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(FRAGMENT_TAG);
            trans.commit();
        }
        super.onResume();
    }

}
