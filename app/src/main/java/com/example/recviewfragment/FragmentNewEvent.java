package com.example.recviewfragment;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//FIREBASE AUTHENTHIFICATION -- https://www.youtube.com/watch?v=TwHmrZxiPA8


public class FragmentNewEvent extends Fragment {

    private View v;

    private String email;
    private String eventName;
    private String login;
    private String password;
    public LatLng location;

    private MaterialButton btnNewEvent;
    private TextInputLayout etEventName;
    private TextInputLayout etLogin;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;
    private TextInputLayout etEmail;

    private boolean isLoginExists;
    FirebaseAuth firebaseAuth;
    private JsonPlaceHolder jsonPlaceHolder;
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

        initializeUi();

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                location = place.getLatLng();
                locationIsSelected = true;
            }

            @Override
            public void onError(@NonNull Status status) {
//                Toast.makeText(getActivity()
//                        .getApplicationContext(), "Error ! " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(v)){
                    signInHost();
                }
                else{
                    Toast.makeText(getActivity()
                            .getApplicationContext(), "Smth went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        return v;
    }

    //Authorizing User Interface & variables
    public void initializeUi(){
        btnNewEvent = v.findViewById(R.id.btnNewEvent);
        etEventName = v.findViewById(R.id.lName);
        etLogin = v.findViewById(R.id.lLogin);
        etPassword = v.findViewById(R.id.lPassword);
        etConfirmPassword = v.findViewById(R.id.lConfirmPassword);
        etEmail = v.findViewById(R.id.lEmail);

        firebaseAuth = FirebaseAuth.getInstance();

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

    //Removing Google AutocompleteSupportFragment when not needed
    @Override
    public void onDestroyView() {
        if (autocompleteFragment != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(autocompleteFragment).commitNowAllowingStateLoss();
        }
        super.onDestroyView();
    }

    private boolean validateEmailInput(){
        email = etEmail.getEditText().getText().toString().trim();
        if(email.isEmpty()){
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

    private CallbackInterfaceLoginExists callbackInterfaceLoginExists = new CallbackInterfaceLoginExists() {
        @Override
        public boolean onSuccess() {
            login = etLogin.getEditText().getText().toString().trim();

            jsonPlaceHolder = ApiClient.getInterface();
            Call<List<ItemHost>> call = jsonPlaceHolder.getHostByLogin(login);
            call.enqueue(new Callback<List<ItemHost>>() {
                @Override
                public void onResponse(Call<List<ItemHost>> call, Response<List<ItemHost>> response) {
                    if (!(response.body() == null)){
                        isLoginExists = false;
                    }
                    else{
                        isLoginExists = true;
                    }
                }

                @Override
                public void onFailure(Call<List<ItemHost>> call, Throwable t) {
                    Toast.makeText(getActivity()
                            .getApplicationContext(), "Error message" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error code is:", t.getMessage());
                    isLoginExists = true;
                }
            });
            return isLoginExists;
        }
    };

    public boolean validateLoginInput(){
        login = etLogin.getEditText().getText().toString().trim();
        if (login.isEmpty()){
            etLogin.setError("Please provide login");
            return false;
        }
        else if(login.length() > 15){
            etLogin.setError("Login is too long");
            return false;
        }
        else if(callbackInterfaceLoginExists.onSuccess()){
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

    public boolean validatePasswordInput(){
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
            etLogin.setError("Please create password with no spaces");
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

    //Making sure all the fields are being properly entered
    public boolean validateInput(View v){
        if(!validateLocationInput() | !validateEventNameInput() | !validateLoginInput() | !validatePasswordInput() | !validateConfirmPasswordInput() | !validateEmailInput()){
            return false;
        }
        else return true;
    }

    //Adding new host to a Firebase
    public void signInHost(){
        String email = etEmail.getEditText().getText().toString().trim();
        String password = etConfirmPassword.getEditText().getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity()
                                    .getApplicationContext(), "Host is Created", Toast.LENGTH_SHORT).show();
                            callbackInterfaceHostSignIn.onSuccess();
                        }
                        else{
                            Toast.makeText(getActivity()
                                    .getApplicationContext(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity()
                                .getApplicationContext(), "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Adding new host after he/she is being added to Firebase
    private CallbackInterfaceHostSignIn callbackInterfaceHostSignIn = new CallbackInterfaceHostSignIn() {
        @Override
        public void onSuccess() {
            jsonPlaceHolder = ApiClient.getInterface();
            double latitude = location.latitude;
            double longitude = location.longitude;
            ItemHost itemHost = new ItemHost(login, password, email, eventName, latitude, longitude);
            Call <ItemHost> call = jsonPlaceHolder.createArtist(itemHost);
            call.enqueue(new Callback<ItemHost>() {
                @Override
                public void onResponse(Call<ItemHost> call, Response<ItemHost> response) {
                    if(!response.isSuccessful()){
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
    };

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        LatLng location = new LatLng(41.924664, -87.700298);
//        map.addMarker(new MarkerOptions().position(location).title("FDC Studios"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(location));
//    }

}
