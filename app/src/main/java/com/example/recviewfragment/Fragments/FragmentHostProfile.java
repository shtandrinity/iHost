package com.example.recviewfragment.Fragments;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.PreferenceUtils;
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
        etEventName = v.findViewById(R.id.tvProfileEventName);
        etEventAddress = v.findViewById(R.id.tvProfileLocation);
        etlogin = v.findViewById(R.id.tvProfileLogin);
        etEmail = v.findViewById(R.id.tvProfileEmail);
        btnLogout = v.findViewById(R.id.btnProfile_logOut);
        setBackButtonLogic();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapProfile);
        mapFragment.getMapAsync(this);
        preferenceUtils = new PreferenceUtils(getContext());

        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceUtils.clearSavedInSharedPreference();
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//                    fm.popBackStack();
//                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                //fm.getFragments().get(1).getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                for (Fragment frag : fm.getFragments()) {
                        FragmentManager childFm = frag.getChildFragmentManager();
                        if (childFm.getBackStackEntryCount() > 0) {
                            childFm.popBackStack();
                        }
                    childFm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(location).title("Event Title"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(14).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //I cant's go back from Host profile screen
    private void setBackButtonLogic() {
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
                return false;
            }
        });
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
