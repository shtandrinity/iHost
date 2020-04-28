package com.example.recviewfragment.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.recviewfragment.Model.PreferenceUtils;
import com.example.recviewfragment.R;
import com.google.android.material.button.MaterialButton;


public class FragmentHostUnlogged extends Fragment {

    private View v;
    private final String FRAGMENT_TAG = "hostUnlogged_screen";

    public FragmentHostUnlogged(){}

    FragmentHostUnlogged newInstance(){
        return new FragmentHostUnlogged();
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        v = inflater.inflate(R.layout.fragment_host_unlogged, container, false);
        MaterialButton btnNewEvent = (MaterialButton) v.findViewById(R.id.btnNewEvent_host);
        MaterialButton btnLogin_host = (MaterialButton) v.findViewById(R.id.btnLogin_host);

        PreferenceUtils preferenceUtils = new PreferenceUtils(getActivity());                       //If user is logged - send to FragmentHostProfile
        if(preferenceUtils.getBoolean("isLogged")){
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();
            trans.replace(R.id.hostsUlogged_container, new FragmentHostProfile(), "HostUnlogged-HostProfile");
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(FRAGMENT_TAG);
            trans.commit();
        }
         //If user is not logged load FragmentHostUnlogged
        btnLogin_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getChildFragmentManager().beginTransaction();
                trans.replace(R.id.hostsUlogged_container, new FragmentLogin().newInstance(), "Login");
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(FRAGMENT_TAG);
                trans.commit();
            }
        });

        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getChildFragmentManager().beginTransaction();
                trans.replace(R.id.hostsUlogged_container, new FragmentNewHost().newInstance(), "NewEvent");
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(FRAGMENT_TAG);
                trans.commit();
            }
        });
        return v;
    }

//    private void deactivateAllButtons() {
//        LinearLayout rl = v.findViewById(R.id.linear_HostUnloggedContainer);
//        final int childCount = rl.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            if(rl.getChildAt(i) instanceof MaterialButton){
//                rl.getChildAt(i).setClickable(false);
//            }
//        }
//    }
}

