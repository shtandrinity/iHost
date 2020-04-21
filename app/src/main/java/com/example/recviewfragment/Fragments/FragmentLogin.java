package com.example.recviewfragment.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recviewfragment.API.ApiClient;
import com.example.recviewfragment.Interfaces.CallbackInterfaceAddHost;
import com.example.recviewfragment.Model.ItemHost;
import com.example.recviewfragment.API.JsonPlaceHolder;
import com.example.recviewfragment.PreferenceUtils;
import com.example.recviewfragment.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FragmentLogin extends Fragment {

    private String login;
    private String password;
    private TextInputLayout etLogin;
    private TextInputLayout etPassword;
    private final String FRAGMENT_TAG = "login_screen";

    public FragmentLogin() {}

    FragmentLogin newInstance(){
        return new FragmentLogin();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        etLogin = v.findViewById(R.id.lLogin_login);
        etPassword = v.findViewById(R.id.lLogin_Password);
        MaterialButton btnLogin_login = v.findViewById(R.id.btnLogin_login);

        btnLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(v)){
                    validateHost();
                }
            }
        });

        return v;
    }

    private CallbackInterfaceAddHost CallbackInterfaceAddHost = new CallbackInterfaceAddHost() {
        //Sending user to HostProfile fragment only after Login is succesfull
        @Override
        public void onSuccess() {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();

            trans.replace(R.id.loginContainer, FragmentHostProfile.newInstance(), "Login-Profile");
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(FRAGMENT_TAG);

            fm.popBackStackImmediate("hostUnlogged_screen", 0);
            trans.commit();
        }
    };

    //===================================INPUT VALIDATION METHODS===================================
    private boolean validateLoginInput(){
        login = etLogin.getEditText().getText().toString().trim();
        if (login.isEmpty()){
            etLogin.setError("Please provide login");
            return false;
        }
        else{
            etLogin.setError(null);
            return true;
        }
    }

    private boolean validatePasswordInput(){
        password = etPassword.getEditText().getText().toString().trim();
        if (password.isEmpty()){
            etPassword.setError("Please provide the password");
            return false;
        }
        else{
            etPassword.setError(null);
            return true;
        }
    }

    private boolean validateInput(View v){
        return !(!validateLoginInput() | !validatePasswordInput());
    }

    private void validateHost() {
        login = etLogin.getEditText().getText().toString().trim();
        password = etPassword.getEditText().getText().toString().trim();

        JsonPlaceHolder jsonPlaceHolder = ApiClient.getInterface();
        Call<List<ItemHost>> call = jsonPlaceHolder.getHostByLogin(login);
        call.enqueue(new Callback<List<ItemHost>>() {
            @Override
            public void onResponse(Call<List<ItemHost>> call, Response<List<ItemHost>> response) {
                assert response.body() != null;
                if (!(response.body().size() == 0)){
                    if(password.equals(response.body().get(0).getPassword())){

                        PreferenceUtils preferenceUtils = new PreferenceUtils(getContext());
                        String itemHostBody = preferenceUtils.serializeToJson(response.body().get(0));
                        preferenceUtils.setBoolean("isLogged", true);
                        preferenceUtils.setString("itemHost", itemHostBody);
                        preferenceUtils.setInteger("itemHostID", response.body().get(0).getId());
                        preferenceUtils.setString("eventNameToLogged", response.body().get(0).getEventName());
                        CallbackInterfaceAddHost.onSuccess();
                    }
                    else {
                        etPassword.setError("Wrong login or password");
                    }
                }
                else {
                    etPassword.setError("Wrong login or password");
                }
            }

            @Override
            public void onFailure(Call<List<ItemHost>> call, Throwable t) {
                Log.e("Error code is:", t.getMessage());
                etPassword.setError("Wrong login or password");
            }
        });
    }
}

