package com.example.vidbregar.bluepodcast.ui.main;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.example.vidbregar.bluepodcast.R;
import com.example.vidbregar.bluepodcast.util.SharedPreferencesUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private ScreenSlidePagerAdapter screenSlidePagerAdapter;
    private MenuItem prevMenuItem;
    private OnBackPressedListener onBackPressedListener;

    @Inject
    SharedPreferencesUtil sharedPreferencesUtil;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @BindView(R.id.view_pager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    public interface OnBackPressedListener {

        void onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpViewPager();
        setUpBottomNavigation();
        sharedPreferencesUtil.setIsMainActivityAlive(true);
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.menu_search:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.menu_favorites:
                            viewPager.setCurrentItem(2);
                            break;
                    }
                    return false;
                });

    }

    private void setUpViewPager() {
        screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(screenSlidePagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            if (sharedPreferencesUtil.isOnPodcastDetailLayout()) {
                // If the user is currently looking at the podcast detail layout,
                // return back to podcasts list
                onBackPressedListener.onBackPressed();
            } else {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
            }
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferencesUtil.setIsMainActivityAlive(false);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
