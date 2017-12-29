package com.example.kobac.myapplication.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kobac.myapplication.R;
import com.example.kobac.myapplication.ZoomOutPageTransformer;

import java.util.ArrayList;


/**
 * Created by kobac on 20.7.17..
 */

public class DetailsActivity extends AppCompatActivity {


    public static final String BUNDLE_POSITION = "position";
    public static final String BUNDLE_MAP = "map";

    ViewPager mViewPager;

    private DetailsMap mDetailsMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view_pager);

        Intent intent = getIntent();
        mDetailsMap = new DetailsMap((ArrayList<String>) intent.getSerializableExtra(BUNDLE_MAP));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(intent.getIntExtra(BUNDLE_POSITION,0));
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem (int position) {
            final Bundle bundle = new Bundle();
            bundle.putString(DetailsFragment.BUNDLE_URL, mDetailsMap.getURLForPosition(position));
            final DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
        @Override
        public int getCount(){
            return mDetailsMap.getCount();
        }

    }

}