package com.example.recviewfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;

import at.markushi.ui.CircleButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment {

    private MaterialButton btnLogin;
    private CircleButton btnBack;

    private View v;

    public FragmentLogin() {}

    FragmentLogin newInstance(){
        return new FragmentLogin();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = v.findViewById(R.id.btnLogin);
        FragmentLogin fragmentLogin = this.newInstance();

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
}
