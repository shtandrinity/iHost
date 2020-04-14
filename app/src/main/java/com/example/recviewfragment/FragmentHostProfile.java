package com.example.recviewfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHostProfile extends Fragment implements OnMapReadyCallback {
    private View v;
    private TextView etEventName;
    private TextView etEventAddress;
    private TextView etlogin;
    private TextView etEmail;
    private MaterialButton btnLogout;
    private LatLng location;
    GoogleMap map;
    JsonPlaceHolder jsonPlaceHolder;

    private String login;

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

        SharedPreferences spProfile = getContext().getSharedPreferences("LoginShared", Context.MODE_PRIVATE);
        login = spProfile.getString("LoginShared", null);

        Bundle b = this.getArguments();
        if(b != null){
            ItemHost host = (ItemHost) b.getSerializable("host");
            location = new LatLng(host.getLatitude(), host.getLongitude());
            etEventName.setText(host.getEventName());
            etEventAddress.setText(host.getEventName());
            etlogin.setText(host.getLogin());
            etEmail.setText(host.getEmail());
        }
        getHostData();
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

//    I cant's go back from Host profile screen
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

    private View getHostData(){
        jsonPlaceHolder = ApiClient.getInterface();
        Call<List<ItemHost>> call = jsonPlaceHolder.getHostByLogin(login);
        call.enqueue(new Callback<List<ItemHost>>() {
            @Override
            public void onResponse(Call<List<ItemHost>> call, Response<List<ItemHost>> response) {
                if(!(response.body().size() == 0)){
                    ItemHost host = response.body().get(0);
                    location = new LatLng(host.getLatitude(), host.getLongitude());
                    etEventName.setText(host.getEventName());
                    etEventAddress.setText(host.getEventName());
                    etlogin.setText(host.getLogin());
                    etEmail.setText(host.getEmail());
                }
                else{
                    Log.e("Error code is:", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<ItemHost>> call, Throwable t) {
                Log.e("Error code is:", t.getMessage());
            }
        });
        return v;
    }
}
