package com.example.recviewfragment.Fragments;

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.CallbackInterfaces.CallbackInterfaceRetrofit;
import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.PreferenceUtils;
import com.example.recviewfragment.R;
import com.google.android.gms.common.api.Status;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//FIREBASE AUTHENTHIFICATION -- https://www.youtube.com/watch?v=TwHmrZxiPA8


public class FragmentNewHost extends Fragment{

    private View v;

    private String email, eventName, login, password;
    private LatLng location;
    private TextInputLayout etEventName, etLogin, etPassword, etConfirmPassword, etEmail;

    private boolean isLoginExists;
    private JsonPlaceHolder jsonPlaceHolder;
    private boolean locationIsSelected = false;
    private AutocompleteSupportFragment autocompleteFragment;
    private final String FRAGMENT_TAG = "newHost_screen";

    public FragmentNewHost() {}

    FragmentNewHost newInstance(){
        return new FragmentNewHost();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_new_host, container, false);
        MaterialButton btnNewEvent = v.findViewById(R.id.btnNewEvent_NewEvent);
        etEventName = v.findViewById(R.id.lNewEvent_name);
        etLogin = v.findViewById(R.id.lNewEvent_login);
        etPassword = v.findViewById(R.id.lNewEvent_password);
        etConfirmPassword = v.findViewById(R.id.lNewEvent_confirmPassword);
        etEmail = v.findViewById(R.id.lNewEvent_email);

        initializeAutocompleteFragment();

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                location = place.getLatLng();
                locationIsSelected = true;
            }

            @Override
            public void onError(@NonNull Status status) {}
        });
        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForLoginExisting();
            }
        });

        return v;
    }

    private void initializeAutocompleteFragment(){
        autocompleteFragment = (AutocompleteSupportFragment) this.getChildFragmentManager().findFragmentById(R.id.autocomplete);
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.map_key), Locale.US);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteFragment.setHint("Search a Location");

        //If clear button in AutocompleteSupportFragment being pressed - do locationIsSelected = false;
        View clearButton = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button);
        clearButton.setOnClickListener(view -> {
            autocompleteFragment.setText("");
            locationIsSelected = false;
        });
    }

    @Override  //Removing Google AutocompleteSupportFragment when not needed
    public void onDestroyView() {
        if (autocompleteFragment != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(autocompleteFragment).commitNowAllowingStateLoss();
        }
        super.onDestroyView();
    }

    private void createNewEvent(){
        jsonPlaceHolder = ApiClient.getInterface();
        double latitude = location.latitude;
        double longitude = location.longitude;

        login = etLogin.getEditText().getText().toString();
        password = etPassword.getEditText().getText().toString();
        email = etEmail.getEditText().getText().toString();
        eventName = etEventName.getEditText().getText().toString();

        ItemHost itemHost = new ItemHost(login, password, email, eventName, latitude, longitude);
        Call <ItemHost> call = jsonPlaceHolder.createHost(itemHost);
        call.enqueue(new Callback<ItemHost>() {
            @Override
            public void onResponse(Call<ItemHost> call, Response<ItemHost> response) {
                if(response.isSuccessful()){

                    //Setting a sharedPreferences
                    PreferenceUtils preferenceUtils = new PreferenceUtils(getContext());
                    String itemHostBody = preferenceUtils.serializeToJson(response.body());
                    preferenceUtils.setBoolean("isLogged", true);
                    preferenceUtils.setString("itemHost", itemHostBody);
                    preferenceUtils.setInteger("itemHostID", response.body().getId());

                    FragmentTransaction trans = getChildFragmentManager().beginTransaction();
                    trans.replace(R.id.newEventContainer, FragmentHostProfile.newInstance(), "NewHost-Profile");
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(FRAGMENT_TAG);
                    trans.commit();
                }
                else {
                    Toast.makeText(getActivity()
                            .getApplicationContext(), "Code" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemHost> call, Throwable t) {
                Toast.makeText(getActivity()
                        .getApplicationContext(), "Error message" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //===================================INPUT VALIDATION METHODS===================================
    private boolean validateLoginInput(){
        login = etLogin.getEditText().getText().toString().trim();
        if (login.isEmpty()){
            etLogin.setError("Please provide login");
            return false;
        }
        else if(login.length() > 15){
            etLogin.setError("Login is too long");
            return false;
        }
        else if(isLoginExists){
            etLogin.setError("Such a login already exists");
            return false;
        }
        else if(login.contains(" ")){
            etLogin.setError("Please create a login with no spaces");
            return false;
        }
        else{
            etLogin.setError(null);
            return true;
        }
    }

    private boolean validateEventNameInput(){
        eventName = etEventName.getEditText().getText().toString();
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

    private boolean validateEmailInput(){
        email = etEmail.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            etEmail.setError("Please provide an email");
            return false;
        }
        else if(email.contains(" ")){
            etEmail.setError("An email shouldn't contain spaces");
            return false;
        }
        else if(!email.contains("@")){
            etEmail.setError("Please provide an existing email");
            return false;
        }
        else{
            etEmail.setError(null);
            etEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePasswordInput(){
        password = etPassword.getEditText().getText().toString();
        if (password.isEmpty()){
            etPassword.setError("Please provide the password");
            return false;
        }
        else if(password.length() <= 8 || password.length() > 15){
            etPassword.setError("Password should be >8 and <15");
            return false;
        }
        else if(password.contains(" ")){
            etPassword.setError("Please create password with no spaces");
            return false;
        }
        else{
            etPassword.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPasswordInput(){
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

    private boolean validateLocationInput(){
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

    //Making sure all the fields are being properly entered
    private boolean validateInput(View v){
        return !(!validateLocationInput() | !validateEventNameInput() | !validateLoginInput() | !validatePasswordInput() | !validateConfirmPasswordInput() | !validateEmailInput());
    }

    private void checkForLoginExisting(){
        login = etLogin.getEditText().getText().toString().trim();

        jsonPlaceHolder = ApiClient.getInterface();
        Call<List<ItemHost>> call = jsonPlaceHolder.getHostByLogin(login);
        call.enqueue(new Callback<List<ItemHost>>() {
            @Override
            public void onResponse(Call<List<ItemHost>> call, Response<List<ItemHost>> response) {
                assert response.body() != null;
                if(!(response.body().size() == 0)){
                    isLoginExists = true;
                    callbackInterfaceRetrofit.onSuccess(isLoginExists);
                }
                else {
                    isLoginExists = false;
                    callbackInterfaceRetrofit.onSuccess(isLoginExists);
                }
            }

            @Override
            public void onFailure(Call<List<ItemHost>> call, Throwable t) {
                Toast.makeText(getActivity()
                        .getApplicationContext(), "Error message" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error code is:", t.getMessage());
            }
        });
    }

    //validate input & create new host
    private CallbackInterfaceRetrofit callbackInterfaceRetrofit = new CallbackInterfaceRetrofit() {
        @Override
        public void onSuccess(boolean loginExists) {
            if(validateInput(v)){
                createNewEvent();
            }
        }
    };
}
