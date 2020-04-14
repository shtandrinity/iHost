package com.example.recviewfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

public class FragmentHostUnlogged extends Fragment {


    private View v;

    private final String FRAGMENT_TAG = "host_screen";

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

        btnLogin_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
//                trans.setCustomAnimations(R.anim.bottom_to_top, R.anim.top_to_bottom, R.anim.bottom_to_top, R.anim.top_to_bottom);
                trans.replace(R.id.hostsContainer, new FragmentLogin().newInstance(), "Login");
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(FRAGMENT_TAG);
                trans.commit();
            }
        });

        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
//                trans.setCustomAnimations(R.anim.top_to_bottom, R.anim.bottom_to_top, R.anim.top_to_bottom, R.anim.bottom_to_top);
                trans.replace(R.id.hostsContainer, new FragmentNewEvent().newInstance(), "NewEvent");
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(FRAGMENT_TAG);
                trans.commit();
            }
        });

        return v;
    }

}

