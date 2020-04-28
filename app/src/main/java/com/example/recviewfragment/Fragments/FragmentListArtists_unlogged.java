package com.example.recviewfragment.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.Adapters.RvAdapter_listArtists_unlogged;
import com.example.recviewfragment.Interfaces.CallbackInterfaceArtistsList;
import com.example.recviewfragment.Interfaces.CallbackInterfaceDoubleDouble;
import com.example.recviewfragment.Interfaces.FragmentListInterface;
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.Model.PreferenceUtils;
import com.example.recviewfragment.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentListArtists_unlogged extends Fragment implements FragmentListInterface {

    private View v;
    private TextView tvEventNameUnlogged;
    private MaterialButton btnSignUp;
    private Dialog myDialog;
    private TextInputLayout etAddName_Dialog, etAddPhone_Dialog;
    private MaterialButton btnCheckIsLocated_Dialog, btnSignUp_Dialog;
    private ImageView ivIsLocated_Dialog;

    private JsonPlaceHolder jsonPlaceHolder;
    private List<ItemArtist> lstItemArtists = new ArrayList<>();
    private RvAdapter_listArtists_unlogged recyclerAdapter;
    private PreferenceUtils preferenceUtils = null;
    private int hostID;
    private boolean artistIsAtLocation = false;
    private boolean artistDeviceAlreadyInSystem = true;
    private String deviceID;
    private final String FRAGMENT_TAG = "listArtistsUnlogged_screen";

    public FragmentListArtists_unlogged() {}

    public static FragmentListArtists_unlogged newInstance() { return new FragmentListArtists_unlogged(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jsonPlaceHolder = ApiClient.getInterface();
        preferenceUtils = new PreferenceUtils(getContext());

        getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list_artists_unlogged, container, false);
        ////////////
//        preferenceUtils.clearSavedInSharedPreference();
        ////////////
        String id = preferenceUtils.getString("deviceID");

        setRecyclerView();
        setDialogView();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.equals("")){
                    deviceID = UUID.randomUUID().toString();
                    myDialog.show();
                    setDialogLogic();
                }
                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "This device is already signed up", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = toast.getView();

                    view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.WHITE);

                    toast.show();
                }
            }
        });

        return v;
    }

    @Override
    public void setRecyclerView() {
        tvEventNameUnlogged = (TextView) v.findViewById(R.id.tvListArtists_eventName_Unlogged);
        btnSignUp = (MaterialButton) v.findViewById(R.id.btnSignUp_listArtistsUnlogged);
        RecyclerView myRecyclerView = (RecyclerView) v.findViewById(R.id.listArtists_recyclerView_Unlogged);
        recyclerAdapter = new RvAdapter_listArtists_unlogged(getContext(), lstItemArtists);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        myRecyclerView.setAdapter(recyclerAdapter);
    }

    private void setDialogView(){
        myDialog = new Dialog(getContext());
        myDialog.setTitle("Sign Up");
        myDialog.setContentView(R.layout.dialog_add_artist_unlogged);
        etAddName_Dialog = (TextInputLayout) myDialog.findViewById(R.id.tvArtistName_dialog_unlogged);
        etAddPhone_Dialog = (TextInputLayout) myDialog.findViewById(R.id.tvArtisPhone_dialog_unlogged);
        btnCheckIsLocated_Dialog = (MaterialButton) myDialog.findViewById(R.id.btnCheckLocation_dialog_unlogged);
        btnSignUp_Dialog = (MaterialButton) myDialog.findViewById(R.id.btnSignUp_dialog_unlogged);
        ivIsLocated_Dialog = (ImageView) myDialog.findViewById(R.id.ivCheckLocation_dialog_unlogged);
    }

    private void setDialogLogic(){

        btnSignUp_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewArtist();
            }
        });

        btnCheckIsLocated_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BTN 'CheckIsLocated'", " IS CLICKED");
                fetchLastLocation();
            }
        });
    }

    @Override
    public void getList() {
        hostID = preferenceUtils.getInteger("itemHostID");
        Call<List<ItemArtist>> call = jsonPlaceHolder.getArtistsByHostID(hostID);
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

    private void fetchLastLocation(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]                           //If app doesn't have a permissions - ask for them
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    callbackInterfaceDoubleDouble.onSuccess(location.getLatitude(), location.getLongitude());
                }
            }
        });
    }

    private CallbackInterfaceArtistsList callbackInterfaceArtistsList = new CallbackInterfaceArtistsList() {
        //Refresh RecyclerView after having GET call done
        @Override
        public void onSuccess(List<ItemArtist> list) {
            for (int i=0; i<list.size(); i++) {
                lstItemArtists.add(new ItemArtist(
                        list.get(i).getDeviceId(),
                        list.get(i).getName(),
                        list.get(i).getPhone(),
                        list.get(i).getId(),
                        list.get(i).getUserId(),
                        list.get(i).getIsCurrentlyOnStage()));
            }
            String eventName = preferenceUtils.getString("eventName_to_ListOfArtists");
            tvEventNameUnlogged.setText(eventName);
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(String errorCode) {
            Log.e("Error code is:", errorCode);
        }
    };

    @Override
    public void onResume() {
        if(preferenceUtils.getBoolean("isLogged")){
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();
            trans.replace(R.id.listArtistsUnlogged_container, new FragmentListArtists_logged(), "lArtistsLogged-lArtistsUnlogged");
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

    private  void createNewArtist(){
        PreferenceUtils preferenceUtils = new PreferenceUtils(getActivity());
        preferenceUtils.setString("deviceID", deviceID);

        for (int i=0; i<lstItemArtists.size(); i++){
            artistDeviceAlreadyInSystem = lstItemArtists.get(i).getDeviceId().equals(deviceID);
        }
        if(validateInput() && artistIsAtLocation && !artistDeviceAlreadyInSystem){
            ItemArtist newItemArtist = new ItemArtist(
                    deviceID,
                    etAddName_Dialog.getEditText().getText().toString(),
                    etAddPhone_Dialog.getEditText().getText().toString(),
                    hostID,
                    false);
            Call<ItemArtist> call = jsonPlaceHolder.createArtist(newItemArtist);
            call.enqueue(new Callback<ItemArtist>() {
                @Override
                public void onResponse(Call<ItemArtist> call, Response<ItemArtist> response) {
                    Log.d("'Sign Up'", " SUCCESS");
                    lstItemArtists.add(newItemArtist);
                    recyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ItemArtist> call, Throwable t) {
                    Log.d("'Sign Up'", t.getMessage());
                }
            });
        }
        else if(artistDeviceAlreadyInSystem){
            etAddPhone_Dialog.setError("This device is already signed in");
        }
        else {
            ivIsLocated_Dialog.setImageResource(R.drawable.check_question_fail);
        }
    }

    private CallbackInterfaceDoubleDouble callbackInterfaceDoubleDouble = new CallbackInterfaceDoubleDouble() {
        @Override
        public void onSuccess(double lat, double lng) {
            String itemHostBody = preferenceUtils.getString("itemHostEvent");
            ItemHost host = preferenceUtils.deserializeFromJson(itemHostBody);

            validateDistance(lat, lng, host.getLatitude(), host.getLongitude());
        }
    };

    //===================================INPUT VALIDATION METHODS===================================
    private boolean validateArtistNameInput(){
        String artistName = etAddName_Dialog.getEditText().getText().toString();
        if (artistName.isEmpty()){
            etAddName_Dialog.setError("Please provide the name");
            return false;
        }
        else if(artistName.length() > 25){
            etAddName_Dialog.setError("Name is too long");
            return false;
        }
        else if(lstItemArtists.contains(artistName)){
            etAddName_Dialog.setError("Such an artist already exists");
            return false;
        }
        else{
            etAddName_Dialog.setError(null);
            return true;
        }
    }

    private boolean validateArtistPhoneInput(){
        String phoneString = etAddPhone_Dialog.getEditText().getText().toString();
        if (phoneString.isEmpty()){
            etAddPhone_Dialog.setError("Please provide your Phone number");
            return false;
        }
        else if(phoneString.length() < 9 || phoneString.length() >12){
            etAddPhone_Dialog.setError("Please provide legit phone number");
            return false;
        }
        else{
            etAddPhone_Dialog.setError(null);
            return true;
        }
    }

    /** calculates the distance between two locations in MILES */
    private void validateDistance(double lat1, double lng1, double lat2, double lng2) {

        Location locationA = new Location("point A");

        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        artistIsAtLocation = true;
        float distance = locationA.distanceTo(locationB);
        if(distance < 500){
            ivIsLocated_Dialog.setImageResource(R.drawable.check_ok);
        }
        else{
            ivIsLocated_Dialog.setImageResource(R.drawable.check_fail);
        }
    }

    private boolean validateInput(){
        return !(!validateArtistNameInput() | !validateArtistPhoneInput());
    }

}
