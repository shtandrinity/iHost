package com.example.recviewfragment.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.recviewfragment.Fragments.FragmentHostUnlogged;
import com.example.recviewfragment.Fragments.FragmentInsta;
import com.example.recviewfragment.Fragments.FragmentListLogged;
import com.example.recviewfragment.Fragments.FragmentListUnlogged;
import com.example.recviewfragment.PreferenceUtils;

//https://stackoverflow.com/questions/7723964/replace-fragment-inside-a-viewpager

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 3;
    private FragmentManager mFragmentManager;
    private Context mContext;

    public ViewPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentManager = fm;
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new FragmentInsta();
            case 1:
//                if(new PreferenceUtils(mContext).getBoolean("isLogged")){
//                    return new FragmentListLogged();
//                }
                return new FragmentListUnlogged();
            case 2: return new FragmentHostUnlogged();
            default:
                return new FragmentListLogged();
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //if (object instanceof FragmentHostUnlogged)
            return POSITION_NONE;
        //return POSITION_UNCHANGED;
    }
}
