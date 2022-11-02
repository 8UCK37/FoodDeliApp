package com.example.apptest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FoodTabAdapter extends FragmentPagerAdapter {

    int tabCounts;

    public FoodTabAdapter(@NonNull FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts = tabCounts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FoodsFragment(DashNavActivity.indian);
            case 1:
                return new FoodsFragment(DashNavActivity.chinese);
            case 2:
                return new FoodsFragment(DashNavActivity.snacks);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
