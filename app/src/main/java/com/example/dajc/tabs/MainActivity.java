package com.example.dajc.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    //CustomViewPager pager;

    public final int nb = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        pager = (ViewPager) findViewById(R.id.pager);
        //custom pager without swiping
            //pager = (CustomViewPager)findViewById(R.id.pager);
            //pager.setPagingEnabled(false);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(pager);

        pagerAdapter.setTabIcon();

        /*
        moved to first activity
        RunAPI run = new RunAPI(this, this);
        run.execute();
        //WishListFragment.sca.notifyDataSetChanged();
        //GalleryFragment.sca.notifyDataSetChanged();

        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        }

        return true;
    }
/*
    public class NonSwipeableViewPager extends ViewPager {

        public NonSwipeableViewPager(Context context) {
            super(context);
        }

        public NonSwipeableViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            // Never allow swiping to switch between pages
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // Never allow swiping to switch between pages
            return false;
        }
    }
*/
    public class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
        List<Drawable> icons = new ArrayList<Drawable>();
        List<Drawable> iconsHilighted = new ArrayList<>();


        public PagerAdapter(FragmentManager fm) {
            super(fm);
            pager.addOnPageChangeListener(this);

            Drawable icon1 =getApplicationContext().getResources().getDrawable(R.mipmap.ic_lhome_passive);
            Drawable icon1Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_home_active);
            Drawable icon2 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_map_passive);
            Drawable icon2Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_map_active);
            Drawable icon3 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_favorite_passive);
            Drawable icon3Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_favorite_active);
            Drawable icon4 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_gallery_passive);
            Drawable icon4Hilighted= getApplicationContext().getResources().getDrawable(R.mipmap.ic_gallery_active);

            icons.add(icon1);
            icons.add(icon2);
            icons.add(icon3);
            icons.add(icon4);

            iconsHilighted.add(icon1Hilighted);
            iconsHilighted.add(icon2Hilighted);
            iconsHilighted.add(icon3Hilighted);
            iconsHilighted.add(icon4Hilighted);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if (position == 0) {
                fragment = new FicheFragment();
            } else if (position == 1) {
                fragment = new MapFragment();
            } else if (position == 2) {
                fragment = new WishListFragment();
            } else {
                fragment = new GalleryFragment();
            }
            Bundle args = new Bundle();
            args.putInt("id", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return nb;
        }

        @Override
 /*       public CharSequence getPageTitle(int position) {
            String titre;
            if (position == 0) {
                titre = "Oeuvre du jour";


            } else if (position == 1) {
                titre = "Plan";
            } else if (position == 2) {
                titre = "Favoris";
            } else {
                titre = "Galerie";
            }

            return titre;
        }
*/
        public CharSequence getPageTitle(int position) {
            return null;
        }

        public void setTabIcon() {
            for(int i = 0; i < icons.size(); i++) {
                if(i == 0) {
                    //noinspection ConstantConditions
                    tabLayout.getTabAt(i).setIcon(iconsHilighted.get(i));
                }
                else {
                    //noinspection ConstantConditions
                    tabLayout.getTabAt(i).setIcon(icons.get(i));
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < getCount(); i++) {
                if(i == position) {
                    //noinspection ConstantConditions
                    tabLayout.getTabAt(i).setIcon(iconsHilighted.get(i));
                }
                else {
                    //noinspection ConstantConditions
                    tabLayout.getTabAt(i).setIcon(icons.get(i));
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            notifyDataSetChanged();
        }
    }

    public class CustomViewPager extends android.support.v4.view.ViewPager{
        private boolean enabled;

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.enabled = true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return enabled ? super.onTouchEvent(event) : false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return enabled ? super.onInterceptTouchEvent(event) : false;
        }

        public void setPagingEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isPagingEnabled() {
            return enabled;
        }

    }


}
