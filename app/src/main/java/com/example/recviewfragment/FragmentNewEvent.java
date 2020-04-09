package com.example.recviewfragment;

import com.google.android.gms.common.api.Status;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Locale;


public class FragmentNewEvent extends Fragment {

    private View v;
    private MaterialButton btnNewEvent;
    private TextInputLayout etEventName;
    private TextInputLayout etLogin;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;
    private TextInputLayout etEmail;

    private boolean locationIsSelected = false;

    GoogleMap map;
    private AutocompleteSupportFragment autocompleteFragment;


    public FragmentNewEvent() {}

    FragmentNewEvent newInstance(){
        return new FragmentNewEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_new_event, container, false);
        btnNewEvent = v.findViewById(R.id.btnNewEvent);
        etEventName = v.findViewById(R.id.lName);
        etLogin = v.findViewById(R.id.lLogin);
        etPassword = v.findViewById(R.id.lPassword);
        etConfirmPassword = v.findViewById(R.id.lConfirmPassword);
        etEmail = v.findViewById(R.id.lEmail);


        autocompleteFragment = (AutocompleteSupportFragment) this.getChildFragmentManager().findFragmentById(R.id.autocomplete);
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.map_key), Locale.US);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        View clearButton = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button);
        clearButton.setOnClickListener(view -> {
            autocompleteFragment.setText("");
            locationIsSelected = false;
        });

        autocompleteFragment.setHint("Search a Location");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                final LatLng latLng = place.getLatLng();
                locationIsSelected = true;
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput(v);
            }
        });

//        autocompleteFragment = (AutocompleteSupportFragment) this.getChildFragmentManager().findFragmentById(R.id.autocomplete);
//        autocompleteFragment.setOnPlaceSelectedListener((AutocompleteSupportFragment) this);
//        autocompleteFragment.setHint("Search a Location");

//        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onDestroyView() {
        if (autocompleteFragment != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(autocompleteFragment).commitNowAllowingStateLoss();
        }
        super.onDestroyView();
    }

    private boolean validateEmailInput(){
        String emailInput = etEmail.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            etEmail.setError("Please provide an email");
            return false;
        }
        else{
            etEmail.setError(null);
            etEmail.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateEventNameInput(){
        String eventName = etEventName.getEditText().getText().toString();
        if (eventName.isEmpty()){
            etEventName.setError("Please provide event's name");
            return false;
        }
        else if(eventName.length()>25){
            etLogin.setError("Event name is too long");
            return false;
        }
        else{
            etEventName.setError(null);
            return true;
        }
    }

    public boolean validateLoginInput(){
        String login = etLogin.getEditText().getText().toString();
        if (login.isEmpty()){
            etLogin.setError("Please provide an login");
            return false;
        }
        else if(login.length() > 15){
            etLogin.setError("Login is too long");
            return false;
        }
        else{
            etLogin.setError(null);
            return true;
        }
    }

    public boolean validatePasswordInput(){
        String password = etPassword.getEditText().getText().toString();
        if (password.isEmpty()){
            etPassword.setError("Please provide the password");
            return false;
        }
        else if(password.length() <= 8 || password.length() > 15){
            etPassword.setError("Password should be >8 and <15");
            return false;
        }
        else{
            etPassword.setError(null);
            return true;
        }
    }

    public boolean validateConfirmPasswordInput(){
        String password = etPassword.getEditText().getText().toString();
        String confirmPassword = etConfirmPassword.getEditText().getText().toString();
        if (confirmPassword.isEmpty()){
            etConfirmPassword.setError("Please confirm the password");
            return false;
        }
        else if(!confirmPassword.equals(password)){
            etConfirmPassword.setError("Passwords does not match");
            return false;
        }
        else{
            etConfirmPassword.setError(null);
            return true;
        }
    }

    public boolean validateLocationInput(){
        if (!locationIsSelected){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Location cannot be empty", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            View view = toast.getView();

            //Gets the actual oval background of the Toast then sets the colour filter
            view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            //Gets the TextView from the Toast so it can be editted
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.WHITE);

            toast.show();
            return false;
        }
        else {
            return true;
        }
    }

    public void confirmInput(View v){
        if(!validateLocationInput() | !validateEventNameInput() | !validateLoginInput() | !validatePasswordInput() | !validateConfirmPasswordInput() | !validateEmailInput()){
            return;
        }
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        LatLng location = new LatLng(41.924664, -87.700298);
//        map.addMarker(new MarkerOptions().position(location).title("FDC Studios"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(location));
//    }

}
