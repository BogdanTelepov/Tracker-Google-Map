package com.inventory.traсker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.inventory.traсker.fragments.HistoryFragment;
import com.inventory.traсker.fragments.HomeFragment;

public class MyAdapter extends FragmentPagerAdapter {


    public MyAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new HistoryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
