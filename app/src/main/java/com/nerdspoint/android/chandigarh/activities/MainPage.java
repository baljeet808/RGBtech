package com.nerdspoint.android.chandigarh.activities;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.fragments.Advrts;
import com.nerdspoint.android.chandigarh.fragments.QuickSearchResults;
import com.nerdspoint.android.chandigarh.fragments.profileUpdation;
import com.nerdspoint.android.chandigarh.fragments.shopRegistration;
import com.nerdspoint.android.chandigarh.permissionCheck.checkInternet;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.Timer;
import java.util.TimerTask;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    View view;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    FragmentManager fragmentManager;
    TextView tv_home,tv_maps,tv_notifications,tv_compare,tv_shopManager;
    TabHost host;
    RelativeLayout main_fragment_holder;
    MenuItem menuItem,menuItem1;
    Menu menu;
    BroadcastReceiver br;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        anim = new ScaleAnimation(
                    1f, 2f, // Start and end values for the X axis scaling
                    1f, 2f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(1000);
            anim.setRepeatCount(Animation.INFINITE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        main_fragment_holder=(RelativeLayout) findViewById(R.id.Main_Fragment_Holder);

        tv_compare =(TextView) findViewById(R.id.tv_compare);
        tv_home=(TextView) findViewById(R.id.tv_home);
        tv_maps=(TextView) findViewById(R.id.tv_maps);
        tv_notifications=(TextView) findViewById(R.id.tv_notifications);
        tv_shopManager=(TextView) findViewById(R.id.tv_shopManager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        menu = navigationView.getMenu();
        menuItem= menu.findItem(R.id.nav_netStatus);
        menuItem1 = menu.findItem(R.id.nav_offline);

        checkInternetConnection();

        TabHost.TabSpec spec = host.newTabSpec("Advertisements");
        spec.setContent(R.id.frag_holder);
        spec.setIndicator("Advertisements");
        host.addTab(spec);

        spec = host.newTabSpec("Profile");
        spec.setContent(R.id.frag_holder2);
        spec.setIndicator("Profile");
        host.addTab(spec);

        Advrts advrts = new Advrts();
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_holder,advrts);
        fragmentTransaction.commit();

        profileUpdation updation = new profileUpdation();

        fragmentManager =getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_holder2,updation);
        fragmentTransaction.commit();

    }

    private void checkInternetConnection() {

        if (br == null) {

            br = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();

                    NetworkInfo info = (NetworkInfo) extras
                            .getParcelable("networkInfo");

                    NetworkInfo.State state = info.getState();

                    if (state == NetworkInfo.State.CONNECTED) {
                        menuItem.setTitle("Internet Status > Online");
                        menuItem1.setVisible(true);
                        menuItem1.setVisible(true);
                        menuItem1.setEnabled(true);
                        menuItem1.setIcon(R.drawable.label_blue_new);
                        checkInternet.getCustomInstance(getApplicationContext()).setState(state);
                    } else {
                        menuItem.setTitle("Internet Status > Offline");
                        menuItem1.setEnabled(false);
                        menuItem1.setVisible(false);
                        checkInternet.getCustomInstance(getApplicationContext()).setState(state);
                    }

                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver((BroadcastReceiver) br, intentFilter);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void bottomToolbar(View v)
    {
        switch(v.getId())
        {
            case R.id.tv_home :
            {
                host.setVisibility(View.VISIBLE);
                main_fragment_holder.setVisibility(View.GONE);
                v.setBackgroundResource(R.drawable.topdownborderbackground);
                tv_compare.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_maps.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_notifications.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_shopManager.setBackgroundResource(R.drawable.backgraoundwithborder);
            }break;
            case R.id.tv_shopManager :
            {
                v.setBackgroundResource(R.drawable.topdownborderbackground);
                tv_compare.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_maps.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_notifications.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_home.setBackgroundResource(R.drawable.backgraoundwithborder);
            }break;
            case R.id.tv_maps :
            {

                v.setBackgroundResource(R.drawable.topdownborderbackground);
                tv_compare.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_home.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_notifications.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_shopManager.setBackgroundResource(R.drawable.backgraoundwithborder);
            }break;
            case R.id.tv_notifications :
            {

                v.setBackgroundResource(R.drawable.topdownborderbackground);
                tv_compare.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_home.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_maps.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_shopManager.setBackgroundResource(R.drawable.backgraoundwithborder);
            }break;
            case R.id.tv_compare :
            {

                v.setBackgroundResource(R.drawable.topdownborderbackground);
                tv_maps.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_home.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_notifications.setBackgroundResource(R.drawable.backgraoundwithborder);
                tv_shopManager.setBackgroundResource(R.drawable.backgraoundwithborder);
            }break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_addShop) {
            main_fragment_holder.setVisibility(View.VISIBLE);
            host.setVisibility(View.GONE);
            shopRegistration registration = new shopRegistration();
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.Main_Fragment_Holder,registration);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_slideshow) {

            main_fragment_holder.setVisibility(View.VISIBLE);
            host.setVisibility(View.GONE);
            QuickSearchResults searchResults = new QuickSearchResults();
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.Main_Fragment_Holder,searchResults);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_manage) {
            ActiveUserDetail.getCustomInstance(getApplicationContext()).logoutUser();
            Intent o = new Intent(MainPage.this,LoginActivity.class);
            startActivity(o);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_offline) {

        }
        else if(id == R.id.nav_netStatus)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
