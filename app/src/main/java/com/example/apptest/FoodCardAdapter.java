package com.example.apptest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FoodCardAdapter extends FragmentPagerAdapter {

    int tabCounts;

    public FoodCardAdapter(@NonNull FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts = tabCounts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FoodsFragment();
            case 1:
                return new FoodsFragment();
            case 2:
                return new FoodsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
