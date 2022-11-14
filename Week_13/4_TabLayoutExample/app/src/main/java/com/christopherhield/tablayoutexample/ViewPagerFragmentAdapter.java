package com.christopherhield.tablayoutexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;

    private final int numTabs;

    ViewPagerFragmentAdapter(MainActivity mainActivity, int numTabs) {
        super(mainActivity);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (fragmentOne == null) {
                    fragmentOne = new FragmentOne();
                }
                return fragmentOne;
            case 1:
                if (fragmentTwo == null) {
                    fragmentTwo = new FragmentTwo();
                }
                return fragmentTwo;
            case 2:
            default:
                if (fragmentThree == null) {
                    fragmentThree = new FragmentThree();
                }
                return fragmentThree;
        }
    }

    @Override
    public int getItemCount() {
        return numTabs;
    }

}
