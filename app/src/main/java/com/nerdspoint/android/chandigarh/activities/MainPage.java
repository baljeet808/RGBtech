package com.nerdspoint.android.chandigarh.activities;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.adapters.populateSearchArray;
import com.nerdspoint.android.chandigarh.fragments.Advrts;
import com.nerdspoint.android.chandigarh.fragments.EditProfile;
import com.nerdspoint.android.chandigarh.fragments.QuickSearchResults;
import com.nerdspoint.android.chandigarh.fragments.ShopPage;
import com.nerdspoint.android.chandigarh.fragments.profileUpdation;
import com.nerdspoint.android.chandigarh.fragments.shopRegistration;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.permissionCheck.checkInternet;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.util.ArrayList;

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
    MenuItem menuItem;
    Menu menu;
    BroadcastReceiver br;
    RelativeLayout popPup,compareLayout;
    Toolbar toolbar;
    Animation animFadein;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    TabHost.TabSpec spec;
    FrameLayout tabContent;
    LinearLayout layout,layout2,layout3,layout4;
    String count="0";
    TextView textView,Name,userType,searchType;
    DBHandler db;
    AutoCompleteTextView searchBar;
    ArrayList<String> items,itemsCopy;
    String temp="Shops",shopID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = new DBHandler(getApplicationContext());

        searchBar=(AutoCompleteTextView) toolbar.findViewById(R.id.et_quickSearch);
        searchType=(TextView) toolbar.findViewById(R.id.tv_search_type);

        if(ActiveUserDetail.getCustomInstance(getApplicationContext()).getIsFirstSync()) {

            try {

                db.syncOffline(searchBar);
                 }
                  catch (Exception e)
                 {
                   Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
            }
            else
            {
                try {
                    populateSearchArray.getCustomInstance(getApplicationContext(), searchBar).populate(temp);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

            }

        items=new ArrayList<String>();
        itemsCopy=new ArrayList<String>();



        host = (TabHost)findViewById(R.id.tabHost);

        host.setup();

        tabContent=(FrameLayout) findViewById(android.R.id.tabcontent);

        main_fragment_holder=(RelativeLayout) findViewById(R.id.Main_Fragment_Holder);
        main_fragment_holder.setVisibility(View.INVISIBLE);

        compareLayout=(RelativeLayout) findViewById(R.id.compare_main_frag);
        compareLayout.setVisibility(View.INVISIBLE);

        final QuickSearchResults searchResults = new QuickSearchResults();

        layout3 = new LinearLayout(getApplicationContext());
        layout3.setId(R.id.my_layout3);
        tabContent.addView(layout3);
        //  checkInternetConnection();


        spec = host.newTabSpec("Advertisements");
        spec.setContent(R.id.my_layout3);
        spec.setIndicator("Advertisements");
        host.addTab(spec);

        Advrts advrts = new Advrts();

        fragmentManager =getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.my_layout3,advrts);
        fragmentTransaction.commit();




        layout4 = new LinearLayout(getApplicationContext());
        layout4.setId(R.id.my_layout4);
        tabContent.addView(layout4);

        layout4.setVisibility(View.INVISIBLE);

        spec = host.newTabSpec("Search Result");
        spec.setContent(R.id.my_layout4);
        spec.setIndicator("Search Results");
        host.addTab(spec);


        fragmentManager =getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.my_layout4,searchResults);
        fragmentTransaction.commit();



        tv_compare =(TextView) findViewById(R.id.tv_compare);
        tv_home=(TextView) findViewById(R.id.tv_home);
        tv_maps=(TextView) findViewById(R.id.tv_maps);
        tv_notifications=(TextView) findViewById(R.id.tv_notifications);
        tv_shopManager=(TextView) findViewById(R.id.tv_shopManager);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();




        navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View view = navigationView.getHeaderView(0);

        Name=(TextView) view.findViewById(R.id.Name);
        userType=(TextView) view.findViewById(R.id.UserType);

        Name.setText(ActiveUserDetail.getCustomInstance(getApplicationContext()).getFirstName()+" "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastName());

        userType.setText(ActiveUserDetail.getCustomInstance(getApplicationContext()).getUserType());

        popPup= (RelativeLayout) view.findViewById(R.id.popPup);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);

        textView=(TextView) popPup.findViewById(R.id.count);
        textView.startAnimation(animFadein);
        textView.setText(count);
        popPup.setVisibility(View.GONE);

        popPup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sync();
                popPup.setVisibility(View.GONE);
                Snackbar.make(getCurrentFocus(),"Syncing.. ",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        });

        menu = navigationView.getMenu();
        menuItem= menu.findItem(R.id.nav_netStatus);


        if(ActiveUserDetail.getCustomInstance(getApplicationContext()).getFirstName().equals("nerdspoint"))
        {


            layout2 = new LinearLayout(getApplicationContext());
            layout2.setId(R.id.my_layout2);
            tabContent.addView(layout2);

            spec = host.newTabSpec("Profile");
            spec.setContent(R.id.my_layout2);
            spec.setIndicator("Profile");
            host.addTab(spec);

            profileUpdation updation = new profileUpdation();

            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.my_layout2,updation);
            fragmentTransaction.commit();




        }



        checkInternetConnection();
    }

    public void  showShop(String shopID)
    {
        this.shopID=shopID;

        bottomToolbar(findViewById(R.id.tv_compare));


    }


    public void profile(View v)
    {

        EditProfile editProfile = new EditProfile();
        Toast.makeText(getApplicationContext(),"profile working",Toast.LENGTH_SHORT).show();
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);



        }

        if(layout==null) {
            layout = new LinearLayout(getApplicationContext());
            layout.setId(R.id.my_layout);
            tabContent.addView(layout);


            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.my_layout, editProfile);
            fragmentTransaction.commit();

            spec = host.newTabSpec("Edit Profile");
            spec.setContent(R.id.my_layout);
            spec.setIndicator("Edit profile");
            host.addTab(spec);

        }

        host.setCurrentTabByTag("Edit Profile");
    }



    public void sync()
    {
        db.syncOffline(searchBar);
        Snackbar.make(getCurrentFocus(),"Offline Database Updated",Snackbar.LENGTH_SHORT).show();
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
                        setPopPup(popPup,R.id.count);
                        popPup.setVisibility(View.VISIBLE);

                        textView.startAnimation(animFadein);
                        checkInternet.getCustomInstance(getApplicationContext()).setState(state);
                    } else {
                        menuItem.setTitle("Internet Status > Offline");
                        popPup.setVisibility(View.GONE);
                        checkInternet.getCustomInstance(getApplicationContext()).setState(state);
                    }

                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver((BroadcastReceiver) br, intentFilter);
        }
    }


    public void setPopPup(RelativeLayout popPup,int countID)
    {
       // Toast.makeText(getApplicationContext(),"last shopid "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastShopID()+" Last ProductID "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastProductID()+" Category id "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCategoryID()+" customPID "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCustomPID()+" ",Toast.LENGTH_LONG).show();

        db.getDBStatus(popPup,countID,ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastProductID(),ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastShopID(),ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCategoryID(),ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCustomPID());

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








    public void changeSearchType(View v)
    {
        final AlertDialog.Builder alert =new AlertDialog.Builder(v.getContext());
        alert.setTitle("Select Search Type ");

        String type= searchType.getText().toString();


        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        final CheckBox checkBox = new CheckBox(getApplicationContext());
        checkBox.setText("Product");
        final CheckBox checkBox1 = new CheckBox(getApplicationContext());
        checkBox1.setText("Category");
        final CheckBox checkBox2 = new CheckBox(getApplicationContext());
        checkBox2.setText("Shops");


        if(type.equals("Category"))
        {
                checkBox1.setChecked(true);
        }else if(type.equals("Product"))
        {
            checkBox.setChecked(true);
        }else {
            checkBox2.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp="Product";
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
            }
        });
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = "Category";
                checkBox.setChecked(false);
                checkBox2.setChecked(false);

            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = "Shops";
                checkBox1.setChecked(false);
                checkBox.setChecked(false);

            }
        });

        layout.addView(checkBox);
        layout.addView(checkBox1);
        layout.addView(checkBox2);

        alert.setView(layout);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                    searchType.setText(temp);
                populateSearchArray.getCustomInstance(getApplicationContext(),searchBar).populate(temp);

            }
        });
        alert.show();
    }








    public void bottomToolbar(View v)
    {
        searchBar.setText("");
        if(view!= null) {
            view.clearAnimation();
            view.setBackgroundResource(R.drawable.backgraoundwithborder);
        }
        view = v;
        v.startAnimation(animFadein);
        v.setBackgroundResource(R.drawable.topdownborderbackground);
        switch(v.getId())
        {
            case R.id.tv_home :
            {

                host.setVisibility(View.VISIBLE);
                main_fragment_holder.setVisibility(View.VISIBLE);
                compareLayout.setVisibility(View.GONE);

            }break;
            case R.id.tv_shopManager :
            {

            }break;
            case R.id.tv_maps :
            {

            }break;
            case R.id.tv_notifications :
            {

            }break;
            case R.id.tv_compare :
            {

                ShopPage shopPage= new ShopPage();

                Bundle bundle= new Bundle();
                bundle.putString("SHOPID",shopID);

                shopPage.setArguments(bundle);

                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.compare_main_frag,shopPage);
                fragmentTransaction.commit();

                   main_fragment_holder.setVisibility(View.GONE);
                    host.setVisibility(View.GONE);
                    compareLayout.setVisibility(View.VISIBLE);
               // Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
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
            searchBar.setText("");
            main_fragment_holder.setVisibility(View.VISIBLE);
            host.setVisibility(View.GONE);
            shopRegistration registration = new shopRegistration();
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.Main_Fragment_Holder,registration);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_slideshow) {


        } else if (id == R.id.nav_manage) {
            searchBar.setText("");
            ActiveUserDetail.getCustomInstance(getApplicationContext()).logoutUser();
            Intent o = new Intent(MainPage.this,LoginActivity.class);
            startActivity(o);
            finish();
        } else if (id == R.id.nav_share) {

        }
        else if(id == R.id.nav_netStatus)
        {
            db.getDBStatus(popPup,R.id.count,ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastProductID(),ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastShopID(),ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCategoryID(),ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCustomPID());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
