package com.yehm.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;
import com.yehm.R;
import com.yehm.constants.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EditProfile extends BaseFragment implements IPickResult {
    Unbinder unbinder;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int currentPosition = 0;
    private ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new EditPersonalDetails(), "Personal");
        adapter.addFragment(new EditBankDetails(), "Bank");
        adapter.addFragment(new EditKycDetail(), "KYC");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        Log.e("REACH", "ACTIVITY");
        ((EditKycDetail) adapter.getItem(currentPosition)).onPickResult(pickResult);
        super.onPickResult(pickResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        (adapter.getItem(currentPosition)).onActivityResult(requestCode, resultCode, data);
    }
}