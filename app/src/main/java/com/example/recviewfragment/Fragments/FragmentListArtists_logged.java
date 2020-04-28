package com.example.recviewfragment.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.Interfaces.CallbackInterfaceArtistsList;
import com.example.recviewfragment.Interfaces.FragmentListInterface;
import com.example.recviewfragment.Interfaces.OnItemClickListener;
import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.Model.PreferenceUtils;
import com.example.recviewfragment.R;
import com.example.recviewfragment.Adapters.RvAdapter_listArtists_logged;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListArtists_logged extends Fragment implements FragmentListInterface {

    private View v;
    private TextView tvEventNameLogged;
    private MaterialButton btnSignUp, btnStart, btnStop;
    private Dialog myDialog;
    private TextInputLayout etAddName_Dialog, etAddPhone_Dialog;
    private MaterialButton btnSignUp_Dialog;
    private RecyclerView myRecyclerView;

    private JsonPlaceHolder jsonPlaceHolder;
    private List<ItemArtist> lstItemArtists = new ArrayList<>();
    private RvAdapter_listArtists_logged recyclerAdapter;
    private PreferenceUtils preferenceUtils = null;
    private int selectedPosition = 0;
    private int previousPosition = -1;
    private int hostID;
    private String deviceID;
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

        getList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list_artists_logged, container, false);
        setBackButtonLogic();

        setRecyclerView();
        setDialogView();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.show();
                setDialogLogic();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setText("Next");
                setRecViewHighlightLogic();
            }
        });

        return v;
    }

    public void setRecyclerView(){
        tvEventNameLogged = (TextView) v.findViewById(R.id.tvListArtists_eventName_logged);
        btnSignUp = (MaterialButton) v.findViewById(R.id.btnAddArtist_listArtists_logged);
        btnStart = (MaterialButton) v.findViewById(R.id.btnStart_listArtists_logged);
        btnStop = (MaterialButton) v.findViewById(R.id.btnStop_listArtists_logged);
        myRecyclerView = (RecyclerView) v.findViewById(R.id.listArtists_recyclerView_logged);
        recyclerAdapter = new RvAdapter_listArtists_logged(getContext(), lstItemArtists);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));

        //Setting a logic for moving artists in a list by host;
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                ItemArtist newTargetArtist = lstItemArtists.get(position_dragged);                  //Human Bloom
                ItemArtist newDraggedArtist = lstItemArtists.get(position_target);                  //Thundercat

                //Making sure that user cannot move around Artists who are before Artistu who's currently highlighted
                if (position_dragged<selectedPosition){
                    return false;
                }
                else if(newDraggedArtist==null || newTargetArtist==null ){
                    return false;
                }

                int tempArtistID = newTargetArtist.getId();
                newTargetArtist.setId(newDraggedArtist.getId());
                newDraggedArtist.setId(tempArtistID);

                Collections.swap(lstItemArtists, position_dragged, position_target);
                Call<ItemArtist> callModifyTargetPosition = jsonPlaceHolder.
                        updateArtistFields(lstItemArtists.get(position_target).getId(), newTargetArtist);
                callModifyTargetPosition.enqueue(new Callback<ItemArtist>() {
                    @Override
                    public void onResponse(Call<ItemArtist> call, Response<ItemArtist> response) {
                        Log.d("TargetPosition status", "SUCCESS");

                        Call<ItemArtist> callModifyDraggedPosition = jsonPlaceHolder.
                                updateArtistFields(lstItemArtists.get(position_dragged).getId(), newDraggedArtist);
                        callModifyDraggedPosition.enqueue(new Callback<ItemArtist>() {
                            @Override
                            public void onResponse(Call<ItemArtist> call, Response<ItemArtist> response) {
                                Log.d("TargetDragged status", "SUCCESS");
                                recyclerAdapter.notifyItemMoved(position_dragged, position_target);
                            }

                            @Override
                            public void onFailure(Call<ItemArtist> call, Throwable t) {
                                Log.d("TargetDragged status", "Failure");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ItemArtist> call, Throwable t) {
                        Log.d("TargetPosition status", "Failure");
                    }
                });

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(myRecyclerView);
        myRecyclerView.setAdapter(recyclerAdapter);

        //Setting a logic for deleting an artists by Host
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
            public void onItemHighlight(int position) {

            }

            @Override
            public void onItemClick(int position) {}
        });
    }

    //Setting a logic for highlighting artist who's currently on stage
    private void setRecViewHighlightLogic(){
        if(selectedPosition == 0) {
            //checking if currentEvent already changed
            for(int i=0; i<lstItemArtists.size(); i++){
                if(lstItemArtists.get(i).getIsCurrentlyOnStage()){
                    selectedPosition=i;
                    previousPosition=i-1;
                    break;
                }
            }

            lstItemArtists.get(selectedPosition).setCurrentlyOnStage(true);
            Call<ItemArtist> callArtistUpdate = jsonPlaceHolder.
                    updateArtistFields(lstItemArtists.get(selectedPosition).getId(), lstItemArtists.get(selectedPosition));
            callArtistUpdate.enqueue(new Callback<ItemArtist>() {
                @Override
                public void onResponse(Call<ItemArtist> call, Response<ItemArtist> response) {
                    Log.d("Selected artist #" + response.body().getId(), "UPDATED");
                    recyclerAdapter.notifyItemChanged(selectedPosition);
                    selectedPosition++;
                    previousPosition++;
                }

                @Override
                public void onFailure(Call<ItemArtist> call, Throwable t) {
                    Log.d("Artist", "UPDATE FAILED");
                }
            });
        }
        else if(selectedPosition < lstItemArtists.size()){
            lstItemArtists.get(selectedPosition).setCurrentlyOnStage(true);
            lstItemArtists.get(previousPosition).setCurrentlyOnStage(false);

            Call<ItemArtist> callArtistUpdate = jsonPlaceHolder.
                    updateArtistFields(lstItemArtists.get(previousPosition).getId(), lstItemArtists.get(previousPosition));
            callArtistUpdate.enqueue(new Callback<ItemArtist>() {
                @Override
                public void onResponse(Call<ItemArtist> call, Response<ItemArtist> response) {

                    Call<ItemArtist> callPreviousArtistUpdate = jsonPlaceHolder.
                            updateArtistFields(lstItemArtists.get(selectedPosition).getId(), lstItemArtists.get(selectedPosition));
                    callPreviousArtistUpdate.enqueue(new Callback<ItemArtist>() {
                        @Override
                        public void onResponse(Call<ItemArtist> call, Response<ItemArtist> response) {
                            Log.d("Selected artist #" + response.body().getId(), "UPDATED");
                            //myRecyclerView.findViewHolderForAdapterPosition(previousPosition);
                            recyclerAdapter.notifyDataSetChanged();
                            selectedPosition++;
                            previousPosition++;
                        }

                        @Override
                        public void onFailure(Call<ItemArtist> call, Throwable t) {
                            Log.d("Selected artist #" + response.body().getId(),"UPDATE FAILED");
                        }
                    });

                    Log.d("Previous artist #" + response.body().getId(), "UPDATED");
                }

                @Override
                public void onFailure(Call<ItemArtist> call, Throwable t) {
                    Log.d("Artist", "UPDATE FAILED");
                }
            });
        }

    }

    private void setDialogView(){
        myDialog = new Dialog(getContext());
        myDialog.setTitle("Sign Up");
        myDialog.setContentView(R.layout.dialog_add_artist_logged);
        etAddName_Dialog = (TextInputLayout) myDialog.findViewById(R.id.tvArtistName_dialog_logged);
        etAddPhone_Dialog = (TextInputLayout) myDialog.findViewById(R.id.tvArtisPhone_dialog_logged);
        btnSignUp_Dialog = (MaterialButton) myDialog.findViewById(R.id.btnSignUp_dialog_logged);
    }

    private void setDialogLogic(){

        btnSignUp_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewArtist();
            }
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
                            list.get(i).getDeviceId(),
                            list.get(i).getName(),
                            list.get(i).getPhone(),
                            list.get(i).getId(),
                            list.get(i).getUserId(),
                            list.get(i).getIsCurrentlyOnStage()));
                }
            String eventName = preferenceUtils.getString("eventName_to_ListOfArtists");
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

    @SuppressLint("ShowToast")
    private  void createNewArtist(){
        PreferenceUtils preferenceUtils = new PreferenceUtils(getActivity());
        preferenceUtils.setString("deviceID", deviceID);

        if(validateArtistNameInput() | validateArtistPhoneInput()){
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
        else {
            Toast.makeText(getActivity(), "Yo :)", Toast.LENGTH_SHORT);
        }
    }

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
}

