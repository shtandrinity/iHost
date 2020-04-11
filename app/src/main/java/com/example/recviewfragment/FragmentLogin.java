package com.example.recviewfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private JsonPlaceHolder jsonPlaceHolder;


    public FragmentLogin() {}

    FragmentLogin newInstance(){
        return new FragmentLogin();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        MaterialButton btnLogin_login = v.findViewById(R.id.btnLogin_login);
        etLogin = v.findViewById(R.id.lLogin_login);
        etPassword = v.findViewById(R.id.lLogin_Password);

        btnLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(v)){
                    login = etLogin.getEditText().getText().toString().trim();
                    password = etPassword.getEditText().getText().toString().trim();

                    jsonPlaceHolder = ApiClient.getInterface();
                    Call<List<ItemHost>> call = jsonPlaceHolder.getHostByLogin(login);
                    call.enqueue(new Callback<List<ItemHost>>() {
                        @Override
                        public void onResponse(Call<List<ItemHost>> call, Response<List<ItemHost>> response) {
                            if(password.equals(response.body().get(0).getPassword())){
                                Toast.makeText(getActivity()
                                        .getApplicationContext(), "Bro, you're logged in", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ItemHost>> call, Throwable t) {
                            Log.e("Error code is:", t.getMessage());
                            etLogin.setError("Wrong login or password");
                            etPassword.setError("Wrong login or password");
                        }
                    });

                }
            }
        });

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction trans = getFragmentManager().beginTransaction();
//                trans.replace(R.id.loginContainer, new FragmentHost().newInstance()).addToBackStack("host_screen");
//                trans.commit();
//            }
//        });

        return v;
    }


    public boolean validateLoginInput(){
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

    public boolean validatePasswordInput(){
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

    public boolean validateInput(View v){
        if(!validateLoginInput() | !validatePasswordInput()){
            return false;
        }
        else return true;
    }
}
