package com.example.recviewfragment.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recviewfragment.R;

public class FragmentInsta extends Fragment {

    private View v;

    public FragmentInsta() {}

    public FragmentInsta newInstance (){
        return new FragmentInsta();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_insta, container, false);
        return v;
    }
}

