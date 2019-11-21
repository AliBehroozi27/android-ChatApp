package com.example.ruzbeh.chatapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.ruzbeh.chatapp.Fragments.LoginFragment;
import com.example.ruzbeh.chatapp.Fragments.SignUpFragment;

import java.io.Serializable;

public class SignUpVPAdapter extends FragmentPagerAdapter implements Serializable {
    private int PAGE_NUMS = 2;

    public SignUpVPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new SignUpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMS;
    }



}