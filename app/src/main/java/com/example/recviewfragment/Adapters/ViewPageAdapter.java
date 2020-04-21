package com.example.recviewfragment.Adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.recviewfragment.Fragments.FragmentHostUnlogged;
import com.example.recviewfragment.Fragments.FragmentInsta;
import com.example.recviewfragment.Fragments.FragmentListLogged;
import com.example.recviewfragment.Fragments.FragmentListUnlogged;

import java.util.HashMap;

//https://stackoverflow.com/questions/7723964/replace-fragment-inside-a-viewpager

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 3;
    private FragmentManager mFragmentManager;
    private Context mContext;
    HashMap<Integer, String> mFragmentTags;

    public ViewPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer,String>();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new FragmentInsta();
            case 1: return new FragmentListUnlogged();
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
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }
}
