package com.example.recviewfragment.Fragments;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.Model.PreferenceUtils;
import com.example.recviewfragment.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class FragmentHostProfile extends Fragment implements OnMapReadyCallback {
    private View v;
    private TextView etEventName, etEmail, etlogin, etEventAddress;
    private MaterialButton btnLogout;
    private LatLng location;
    private GoogleMap map;
    private PreferenceUtils preferenceUtils;


    public FragmentHostProfile(){}

    static FragmentHostProfile newInstance() {
        return new FragmentHostProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_host_profile, container, false);

        setupUI();

        try { getData(); }
        catch (IOException e) { e.printStackTrace(); }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceUtils.clearSavedInSharedPreference();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (Fragment frag : fm.getFragments()) {
                        FragmentManager childFm = frag.getChildFragmentManager();
                        if (childFm.getBackStackEntryCount() > 0) {
                            childFm.popBackStack();
                        }
                        else childFm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });

        return v;
    }
    private void setupUI(){

        etEventName = v.findViewById(R.id.tvProfileEventName);
        etEventAddress = v.findViewById(R.id.tvProfileLocation);
        etlogin = v.findViewById(R.id.tvProfileLogin);
        etEmail = v.findViewById(R.id.tvProfileEmail);
        btnLogout = v.findViewById(R.id.btnProfile_logOut);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapProfile);
        mapFragment.getMapAsync(this);
        preferenceUtils = new PreferenceUtils(getContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(location).title("Event Title"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(16).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    //getting login data from sharedPreferences
    @SuppressLint("SetTextI18n")
    private void getData() throws IOException {

        String itemHostBody = preferenceUtils.getString("itemHost");
        ItemHost itemHost = preferenceUtils.deserializeFromJson(itemHostBody);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        location = new LatLng(itemHost.getLatitude(), itemHost.getLongitude());
        List<Address> addressGoogle = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        String address = addressGoogle.get(0).getAddressLine(0);

        etEventAddress.setText("Address: " + address);
        etEventName.setText("Event Name: " + itemHost.getEventName());
        etlogin.setText("Login: " + itemHost.getLogin());
        etEmail.setText("Email: " + itemHost.getEmail());
    }
}
