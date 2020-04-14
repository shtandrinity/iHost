package com.example.recviewfragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//https://stackoverflow.com/questions/7723964/replace-fragment-inside-a-viewpager

public class ViewPageAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;
    private FragmentManager mFragmentManager;
    private Fragment mFragmentAtPos2;

    public interface FragmentHostFragmentListener
    {
        void onSwitchToNextFragment();
    }

    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FragmentInsta();
            case 1: return new FragmentList();
            case 2:
//                if(mFragmentAtPos2 == null)
//                {
//                    mFragmentAtPos2 = new FragmentHost().newInstance(new FragmentHostFragmentListener() {
//                        @Override
//                        public void onSwitchToNextFragment() {
//                            mFragmentManager.beginTransaction().remove(mFragmentAtPos2).commit();
//                            mFragmentAtPos2 = new FragmentLogin().newInstance();
//                            notifyDataSetChanged();
//                        }
//                    });
//                }
//                return mFragmentAtPos2;
                return new FragmentHostUnlogged().newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof FragmentHostUnlogged && mFragmentAtPos2 instanceof FragmentLogin)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }
}
