package com.example.recviewfragment.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.recviewfragment.Fragments.FragmentHostProfile;
import com.example.recviewfragment.Fragments.FragmentHostUnlogged;
import com.example.recviewfragment.Fragments.FragmentInsta;
import com.example.recviewfragment.Fragments.FragmentList;
import com.example.recviewfragment.Fragments.FragmentLogin;
import com.example.recviewfragment.PreferenceUtils;

//https://stackoverflow.com/questions/7723964/replace-fragment-inside-a-viewpager

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 3;
    private FragmentManager mFragmentManager;
    private Context mContext;

    public ViewPageAdapter(@NonNull FragmentManager fm, Context c) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentManager = fm;
        mContext = c;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        boolean isLogged = new PreferenceUtils(mContext).getBoolean("isLogged");
        switch (position){
            case 0: return new FragmentInsta();
            case 1: return new FragmentList();
            case 2: return new FragmentHostUnlogged();
//                if(isLogged){
//                    return new FragmentHostProfile();
//                }
//                else{
//                    return new FragmentHostUnlogged();
//                }
            default:
                return new FragmentHostProfile();
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
