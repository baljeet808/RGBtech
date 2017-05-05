package com.nerdspoint.android.chandigarh.activities;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.adapters.GPSTracker;
import com.nerdspoint.android.chandigarh.adapters.GetImages;
import com.nerdspoint.android.chandigarh.adapters.MapHistoryAdapter;
import com.nerdspoint.android.chandigarh.adapters.TempShopAdapter;
import com.nerdspoint.android.chandigarh.adapters.populateSearchArray;

import com.nerdspoint.android.chandigarh.fragments.Advrts;
import com.nerdspoint.android.chandigarh.fragments.EditProfile;
import com.nerdspoint.android.chandigarh.fragments.ImageHandler;
import com.nerdspoint.android.chandigarh.fragments.Notification;
import com.nerdspoint.android.chandigarh.fragments.ProductAdd;
import com.nerdspoint.android.chandigarh.fragments.QuickSearchResults;
import com.nerdspoint.android.chandigarh.fragments.ShopInfo;
import com.nerdspoint.android.chandigarh.fragments.ShopManager;
import com.nerdspoint.android.chandigarh.fragments.ShopPage;
import com.nerdspoint.android.chandigarh.fragments.categoriesMenu;
import com.nerdspoint.android.chandigarh.fragments.createMessage;
import com.nerdspoint.android.chandigarh.fragments.customProductList;
import com.nerdspoint.android.chandigarh.fragments.productMenu;
import com.nerdspoint.android.chandigarh.fragments.profileUpdation;
import com.nerdspoint.android.chandigarh.fragments.shopRegistration;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.permissionCheck.checkInternet;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;


import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    public int backcount;
    NavigationView navigationView;
    View view;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    TextView tv_home, tv_maps, tv_notifications, tv_compare, tv_shopManager;
    TabHost host;
    RelativeLayout main_fragment_holder, ShopManager_layout, Notification_layout, Compare_layout;
    LinearLayout Maps_layout;
    MenuItem menuItem;
    Menu menu;
    BroadcastReceiver br;
    RelativeLayout popPup, compareLayout, content_main;
    Toolbar toolbar;
    Animation animFadein;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    TabHost.TabSpec spec;
    FrameLayout tabContent;
    LinearLayout layout, layout2, layout3, layout4;
    String count = "0";
    TextView textView, Name, userType, searchType;
    DBHandler db;
    DrawerLayout mainPage;
    AutoCompleteTextView searchBar;
    ArrayList<String> items, itemsCopy, shopHistory;
    List<ShopDetails> map_history;

    String temp = "Shops", shopID = "1", ShopName = "nerdspoint";
    FloatingActionButton floatingButton;
    boolean fragmentFlag = true;
    boolean fragmentFlag1 = true;
    AlertDialog dialog, dialog1;
    boolean actionButtonFlag = true;
    boolean flag2 = true;
    ListView result_list, map_history_list;
    LayoutInflater inflater;
    TempShopAdapter tempAdapter;
    View historylist;
    Button clear;
    Boolean flag = true;
    GoogleMap mMap;
    GPSTracker tracker;
    ShopDetails details;
    MapHistoryAdapter historyAdapter;
    String backStack = "";


    Context context;
    String ID,tableName;
    ImageView view0,view1,view2,ProfilePicture;


    String url = "https://baljeet808singh.000webhostapp.com/chandigarh/images/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        tracker = new GPSTracker(getApplicationContext());

        mainPage = (DrawerLayout) findViewById(R.id.drawer_layout);
        inflater = getLayoutInflater();

        historylist = inflater.inflate(R.layout.history_list, null);
        map_history_list = (ListView) findViewById(R.id.map_history_list);

        map_history = new ArrayList<>();

        Cursor cursor = new DBHandler(getApplicationContext()).getMapHistory();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                details = new ShopDetails();
                details.shopName = cursor.getString(cursor.getColumnIndex("ShopName"));
                details.ShopID = cursor.getString(cursor.getColumnIndex("ShopID"));
                map_history.add(details);
                cursor.moveToNext();
            }
        }
        historyAdapter = new MapHistoryAdapter(getApplicationContext(), map_history);
        map_history_list.setAdapter(historyAdapter);

        map_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setAlpha(0.5f);
                ShopDetails shopDetails = map_history.get(position);
                Cursor cursor1 = new DBHandler(getApplicationContext()).getShopByID(shopDetails.ShopID);
                if (cursor1.moveToFirst()) {
                    driveToShop(Double.valueOf(cursor1.getString(cursor1.getColumnIndex("Latitude"))), Double.valueOf(cursor1.getString(cursor1.getColumnIndex("Longitude"))));
                }
            }
        });

        clear = (Button) historylist.findViewById(R.id.clearAll_button);

        floatingButton = (FloatingActionButton) findViewById(R.id.float_button);
        result_list = (ListView) historylist.findViewById(R.id.result_list);


        shopHistory = new ArrayList<String>();
        tempAdapter = new TempShopAdapter(getApplicationContext(), shopHistory);

        result_list.setAdapter(tempAdapter);

        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.s_id);

                showShop(textView.getText().toString(), null);
                dialog1.dismiss();
                Blurry.delete((ViewGroup) compareLayout.getRootView());
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopHistory.clear();
                tempAdapter.notifyDataSetChanged();
                clear.setVisibility(View.INVISIBLE);
            }
        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Blurry.with(getApplicationContext()).radius(25).sampling(2).onto((ViewGroup) compareLayout.getRootView());
                // compareLayout.setVisibility(View.INVISIBLE);
                if (dialog1 == null) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainPage.this);


                    alert.setView(historylist);
                    alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Blurry.delete((ViewGroup) mainPage.getRootView());
                        }
                    });
                    dialog1 = alert.create();
                }
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();


            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHandler(getApplicationContext());
        content_main = (RelativeLayout) findViewById(R.id.content_main_page);

        searchBar = (AutoCompleteTextView) toolbar.findViewById(R.id.et_quickSearch);
        searchType = (TextView) toolbar.findViewById(R.id.tv_search_type);


        items = new ArrayList<String>();
        itemsCopy = new ArrayList<String>();


        host = (TabHost) findViewById(R.id.tabHost);

        host.setup();

        tabContent = (FrameLayout) findViewById(android.R.id.tabcontent);

        main_fragment_holder = (RelativeLayout) findViewById(R.id.Main_Fragment_Holder);
        main_fragment_holder.setVisibility(View.INVISIBLE);

        ShopManager_layout = (RelativeLayout) findViewById(R.id.Shop_manager_holder);
        ShopManager_layout.setVisibility(View.INVISIBLE);


        Notification_layout = (RelativeLayout) findViewById(R.id.Notification_holder);
        Notification_layout.setVisibility(View.INVISIBLE);


        Maps_layout = (LinearLayout) findViewById(R.id.maps_holder);
        Maps_layout.setVisibility(View.INVISIBLE);

        compareLayout = (RelativeLayout) findViewById(R.id.compare_main_frag);
        compareLayout.setVisibility(View.INVISIBLE);

        setTabHost();

        tv_compare = (TextView) findViewById(R.id.tv_compare);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_maps = (TextView) findViewById(R.id.tv_maps);
        tv_notifications = (TextView) findViewById(R.id.tv_notifications);
        tv_shopManager = (TextView) findViewById(R.id.tv_shopManager);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(getApplicationContext(), "working", Toast.LENGTH_SHORT).show();
                updateDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View view = navigationView.getHeaderView(0);

        Name = (TextView) view.findViewById(R.id.Name);
        userType = (TextView) view.findViewById(R.id.UserType);
        ProfilePicture = (ImageView) view.findViewById(R.id.imageView);

        Name.setText(ActiveUserDetail.getCustomInstance(getApplicationContext()).getFirstName() + " " + ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastName());

        userType.setText(ActiveUserDetail.getCustomInstance(getApplicationContext()).getUserType());

        popPup = (RelativeLayout) view.findViewById(R.id.popPup);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);

        textView = (TextView) popPup.findViewById(R.id.count);
        textView.startAnimation(animFadein);
        textView.setText(count);

        popPup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sync();
                popPup.setVisibility(View.GONE);
                Snackbar.make(getCurrentFocus(), "Syncing.. ", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        menu = navigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_netStatus);


        if (ActiveUserDetail.getCustomInstance(getApplicationContext()).getIsFirstSync()) {

            try {
                Toast.makeText(this, "syncing for first time", Toast.LENGTH_LONG).show();
                new DBHandler(MainPage.this, getApplicationContext()).syncOffline(searchBar);
                popPup.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                populateSearchArray.getCustomInstance(getApplicationContext(), searchBar).populate(searchType.getText().toString());

            } catch (Exception e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }



        Notification notification = new Notification();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


        fragmentTransaction.add(R.id.Notification_holder, notification);
        fragmentTransaction.commit();


        ShopPage shopPage = new ShopPage();

        Bundle bundle = new Bundle();
        bundle.putString("SHOPID", "0");

        shopPage.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.add(R.id.compare_main_frag, shopPage);
        fragmentTransaction.commit();


        ImageHandler imageHandler = new ImageHandler();

        Bundle bundle1 = new Bundle();
        bundle1.putString("ID", "");
        bundle1.putString("TableName", "");


        imageHandler.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Main_Fragment_Holder, imageHandler);
        fragmentTransaction.commit();


        checkInternetConnection();
        if(isNetworkAvailable())
        {
            checkInternet.getCustomInstance(getApplicationContext()).setState(true);
        }
        else
        {
            checkInternet.getCustomInstance(getApplicationContext()).setState(false);
        }

        updateDrawer();
    }


    public void setTabHost()
    {
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

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.my_layout3, advrts);
        fragmentTransaction.commit();


        layout4 = new LinearLayout(getApplicationContext());
        layout4.setId(R.id.my_layout4);
        tabContent.addView(layout4);

        layout4.setVisibility(View.INVISIBLE);

        spec = host.newTabSpec("Search Result");
        spec.setContent(R.id.my_layout4);
        spec.setIndicator("Search Results");
        host.addTab(spec);


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.my_layout4, searchResults);
        fragmentTransaction.commit();

        if (ActiveUserDetail.getCustomInstance(getApplicationContext()).getFirstName().equals("nerdspoint")) {


            layout2 = new LinearLayout(getApplicationContext());
            layout2.setId(R.id.my_layout2);
            tabContent.addView(layout2);

            spec = host.newTabSpec("Profile");
            spec.setContent(R.id.my_layout2);
            spec.setIndicator("Profile");
            host.addTab(spec);

            profileUpdation updation = new profileUpdation();

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.my_layout2, updation);
            fragmentTransaction.commit();

        }


    }

    public void updateDrawer()
    {


        if(checkInternet.getCustomInstance(getApplicationContext()).isConnected()) {
            String temp = url;
            String name = ActiveUserDetail.getCustomInstance(getApplicationContext()).getUserImageName();
            if(name!="null") {
                temp = temp + "" + name;
                Glide.with(getApplicationContext()).load(temp)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ProfilePicture);
                 }
        }
        else{
            Toast.makeText(getApplicationContext(), "drawer not updated", Toast.LENGTH_LONG).show();
        }
    }

    public void setAdds() {
        Advrts advrts = new Advrts();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_layout3, advrts);
        fragmentTransaction.commit();

    }

    public void driveToShop(Double latitude, Double Longitude) {
        try {
            MapsInitializer.initialize(getApplicationContext());

            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Toast.makeText(this, "error setting map " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        searchBar.setText("");
        host.setVisibility(View.INVISIBLE);
        ShopManager_layout.setVisibility(View.INVISIBLE);
        Maps_layout.setVisibility(View.VISIBLE);
        main_fragment_holder.setVisibility(View.INVISIBLE);
        Notification_layout.setVisibility(View.INVISIBLE);
        compareLayout.setVisibility(View.INVISIBLE);
        floatingButton.setVisibility(View.INVISIBLE);


        Cursor cursor = new DBHandler(getApplicationContext()).getAll("Places");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ShopDetails details = new ShopDetails();
                details.shopName = cursor.getString(cursor.getColumnIndex("ShopName"));
                details.ShopID = cursor.getString(cursor.getColumnIndex("ShopID"));
                map_history.add(details);
            }
        }
        try {
            showroute(latitude, Longitude);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error on showrouute Call " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void setCreateMessage(String shopName, String Shopid, String fid,String cpid) {
        createMessage message = new createMessage();

        Bundle bundle = new Bundle();
        bundle.putString("shopid", Shopid);
        bundle.putString("fid", fid);
        bundle.putString("cpid",cpid);
        bundle.putString("shopName",shopName);

        message.setArguments(bundle);

        searchBar.setText("");
        host.setVisibility(View.INVISIBLE);
        ShopManager_layout.setVisibility(View.INVISIBLE);
        Maps_layout.setVisibility(View.INVISIBLE);
        main_fragment_holder.setVisibility(View.INVISIBLE);
        Notification_layout.setVisibility(View.INVISIBLE);
        compareLayout.setVisibility(View.VISIBLE);
        floatingButton.setVisibility(View.INVISIBLE);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.replace(R.id.compare_main_frag, message);

        fragmentTransaction.commit();
    }

    public void setShopAgain(String Shopid) {
        ShopPage shop = new ShopPage();

        Bundle bundle = new Bundle();
        bundle.putString("shopid", Shopid);

        shop.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.replace(R.id.compare_main_frag, shop);

        fragmentTransaction.commit();
    }


    public void ShopInfo(String shopid) {
        Bundle bundle = new Bundle();
        bundle.putString("shopid", shopid);
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.replace(R.id.Shop_manager_holder, shopInfo);
        fragmentTransaction.commit();
    }


    public void AddProducts(String Shopid) {
        ProductAdd productAdd = new ProductAdd();

        Bundle bundle = new Bundle();
        bundle.putString("shopid", Shopid);

        productAdd.setArguments(bundle);


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.replace(R.id.Shop_manager_holder, productAdd);
        fragmentTransaction.commit();

    }


    public void AddShop() {

        shopRegistration registration = new shopRegistration();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.replace(R.id.Shop_manager_holder, registration);
        fragmentTransaction.commit();
        flag = false;

    }

    public void Shopmanger() {
        ShopManager shopManager = new ShopManager();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.replace(R.id.Shop_manager_holder, shopManager);
        fragmentTransaction.commit();
        flag = true;

    }

    public void showShop(String shopID, String ShopName) {

        clear.setVisibility(View.VISIBLE);
        if (ShopName != null) {
            setHistoryList(ShopName, shopID);
            this.ShopName = ShopName;
            this.shopID = shopID;
        }


        host.setVisibility(View.INVISIBLE);
        ShopManager_layout.setVisibility(View.INVISIBLE);
        Maps_layout.setVisibility(View.INVISIBLE);
        main_fragment_holder.setVisibility(View.INVISIBLE);
        Notification_layout.setVisibility(View.INVISIBLE);
        compareLayout.setVisibility(View.VISIBLE);
        floatingButton.setVisibility(View.VISIBLE);


        ShopPage shopPage = new ShopPage();

        Bundle bundle = new Bundle();
        bundle.putString("SHOPID", shopID);

        shopPage.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        fragmentTransaction.replace(R.id.compare_main_frag, shopPage);
        fragmentTransaction.commit();
    }

    public void setHistoryList(String SHOPNAME, String SHOPID) {
        shopHistory.add(SHOPID);
        tempAdapter.notifyDataSetChanged();
    }

    public void setMapHistory(String shopName, String ShopId) {
        details = new ShopDetails();
        details.ShopID = ShopId;
        details.shopName = shopName;
        map_history.add(details);
        historyAdapter.notifyDataSetChanged();

    }


    public void setCustomPRoductstoList(String productName) {
        Bundle bundle = new Bundle();
        bundle.putString("product", productName);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (fragmentFlag1) {
            customProductList productList = new customProductList();
            productList.setArguments(bundle);
            fragmentTransaction.add(R.id.frag_Product_list_holder, productList);
            fragmentFlag1 = false;

        } else {
            customProductList productList = new customProductList();
            productList.setArguments(bundle);
            fragmentTransaction.replace(R.id.frag_Product_list_holder, productList);
        }
        fragmentTransaction.commit();
    }

    public void setProductsToFragmentHolder(String category) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        Bundle bundle = new Bundle();
        bundle.putString("category", category);

        productMenu products = new productMenu();
        products.setArguments(bundle);
        fragmentTransaction.replace(R.id.frag_menu_holder, products);
        fragmentTransaction.commit();
    }

    public void setCategoriesToFragmentHolder(View view) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (fragmentFlag) {
            categoriesMenu categories = new categoriesMenu();


            fragmentTransaction.add(R.id.frag_menu_holder, categories);
            fragmentFlag = false;
        } else {
            categoriesMenu categories = new categoriesMenu();
            fragmentTransaction.replace(R.id.frag_menu_holder, categories);
        }
        fragmentTransaction.commit();
    }

    public void setSearchPageVisibleOnly() {
        host.setVisibility(View.VISIBLE);
        ShopManager_layout.setVisibility(View.INVISIBLE);
        Maps_layout.setVisibility(View.INVISIBLE);
        main_fragment_holder.setVisibility(View.INVISIBLE);
        Notification_layout.setVisibility(View.INVISIBLE);
        compareLayout.setVisibility(View.INVISIBLE);

    }

    public void setProfilePictureParams(Context context,String id,String table,ImageView view,ImageView view1,ImageView view2)
    {
     this.context = context;
        ID=id;
        tableName = table;
        this.view0=view;
        this.view1=view1;
        this.view2=view2;
    }
    public void SetImage()
    {
        GetImages getImages = new GetImages(context,ID,tableName,view0,null,null);
        getImages.fetchImages();
    }

    public void resetTabs()
    {
        try {
            host.clearAllTabs();
            setTabHost();
            layout=null;
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "unable to close tab "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void profile(View v) {

        EditProfile editProfile = new EditProfile();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        }

        if (layout == null) {
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
        else
        {
            Toast.makeText(getApplicationContext(), "layout is not null", Toast.LENGTH_SHORT).show();
        }

        host.setCurrentTabByTag("Edit Profile");
    }


    public void sync() {
        db.syncOffline(searchBar);
        Snackbar.make(getCurrentFocus(), "Offline Database Updated", Snackbar.LENGTH_SHORT).show();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                        menuItem.setTitle("Check New Data");
                        setPopPup(popPup, R.id.count);


                        textView.startAnimation(animFadein);
                        checkInternet.getCustomInstance(getApplicationContext()).setState(true);
                    } else {
                        menuItem.setTitle("Check New Data > Offline");
                        popPup.setVisibility(View.GONE);
                        checkInternet.getCustomInstance(getApplicationContext()).setState(false);
                    }

                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver((BroadcastReceiver) br, intentFilter);
        }
    }


    public void setPopPup(RelativeLayout popPup, int countID) {
        // Toast.makeText(getApplicationContext(),"last shopid "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastShopID()+" Last ProductID "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastProductID()+" Category id "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCategoryID()+" customPID "+ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCustomPID()+" ",Toast.LENGTH_LONG).show();

        db.getDBStatus(popPup, countID, ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastProductID(), ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastShopID(), ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCategoryID(), ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCustomPID());

    }

    @Override
    public void onBackPressed() {


        if (backcount > 0) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Press again to exits", Toast.LENGTH_LONG).show();
            backcount++;
            new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    backcount = 0;
                }

            }.start();

        }


       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
*/
    }


    public void changeSearchType(View v) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Select Search Type ");

        String type = searchType.getText().toString();


        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        final CheckBox checkBox = new CheckBox(getApplicationContext());
        checkBox.setText("Product");

        final CheckBox checkBox2 = new CheckBox(getApplicationContext());
        checkBox2.setText("Shops");

        if (type.equals("Product")) {
            checkBox.setChecked(true);
        } else {
            checkBox2.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = "Product";

                checkBox2.setChecked(false);
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = "Shops";

                checkBox.setChecked(false);

            }
        });

        layout.addView(checkBox);

        layout.addView(checkBox2);

        alert.setView(layout);
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Blurry.delete((ViewGroup) content_main.getRootView());
            }
        });
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                searchType.setText(temp);

                Blurry.delete((ViewGroup) content_main.getRootView());
                populateSearchArray.getCustomInstance(getApplicationContext(), searchBar).populate(temp);

            }
        });
        alert.show();
        Blurry.with(getApplicationContext()).radius(25).sampling(2).onto((ViewGroup) content_main.getRootView());

    }


    public void bottomToolbar(View v) {
        searchBar.setText("");
        if (view != null) {
            view.clearAnimation();
            view.setBackgroundResource(R.drawable.backgraoundwithborder);
        }
        view = v;
        v.startAnimation(animFadein);
        v.setBackgroundResource(R.drawable.topdownborderbackground);
        switch (v.getId()) {
            case R.id.tv_home: {
                host.setVisibility(View.VISIBLE);
                ShopManager_layout.setVisibility(View.INVISIBLE);
                Maps_layout.setVisibility(View.INVISIBLE);
                main_fragment_holder.setVisibility(View.INVISIBLE);
                Notification_layout.setVisibility(View.INVISIBLE);
                compareLayout.setVisibility(View.INVISIBLE);
                floatingButton.setVisibility(View.INVISIBLE);

            }
            break;
            case R.id.tv_shopManager: {
                if (flag2) {
                    ShopManager manager = new ShopManager();


                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                    fragmentTransaction.add(R.id.Shop_manager_holder, manager);
                    fragmentTransaction.commit();
                    flag2 = false;
                } else {

                    ShopManager manager = new ShopManager();


                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                    fragmentTransaction.replace(R.id.Shop_manager_holder, manager);
                    fragmentTransaction.commit();
                }

                searchBar.setText("");
                host.setVisibility(View.INVISIBLE);
                ShopManager_layout.setVisibility(View.VISIBLE);
                Maps_layout.setVisibility(View.INVISIBLE);
                main_fragment_holder.setVisibility(View.INVISIBLE);
                Notification_layout.setVisibility(View.INVISIBLE);
                compareLayout.setVisibility(View.INVISIBLE);
                floatingButton.setVisibility(View.INVISIBLE);


            }
            break;
            case R.id.tv_maps: {

                try {

                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                } catch (Exception e) {
                    Toast.makeText(this, "error setting map " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                searchBar.setText("");
                host.setVisibility(View.INVISIBLE);
                ShopManager_layout.setVisibility(View.INVISIBLE);
                Maps_layout.setVisibility(View.VISIBLE);
                main_fragment_holder.setVisibility(View.INVISIBLE);
                Notification_layout.setVisibility(View.INVISIBLE);
                compareLayout.setVisibility(View.INVISIBLE);
                floatingButton.setVisibility(View.INVISIBLE);


                Cursor cursor = new DBHandler(getApplicationContext()).getAll("Places");
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        ShopDetails details = new ShopDetails();
                        details.shopName = cursor.getString(cursor.getColumnIndex("ShopName"));
                        details.ShopID = cursor.getString(cursor.getColumnIndex("ShopID"));
                        map_history.add(details);
                    }
                }


            }
            break;
            case R.id.tv_notifications: {
                searchBar.setText("");
                host.setVisibility(View.INVISIBLE);
                ShopManager_layout.setVisibility(View.INVISIBLE);
                Maps_layout.setVisibility(View.INVISIBLE);
                main_fragment_holder.setVisibility(View.INVISIBLE);
                Notification_layout.setVisibility(View.VISIBLE);
                compareLayout.setVisibility(View.INVISIBLE);
                floatingButton.setVisibility(View.INVISIBLE);

            }
            break;
            case R.id.tv_compare: {
                actionButtonFlag = true;
                floatingButton.setVisibility(View.VISIBLE);
                searchBar.setText("");
                host.setVisibility(View.INVISIBLE);
                ShopManager_layout.setVisibility(View.INVISIBLE);
                Maps_layout.setVisibility(View.INVISIBLE);
                main_fragment_holder.setVisibility(View.INVISIBLE);
                Notification_layout.setVisibility(View.INVISIBLE);
                compareLayout.setVisibility(View.VISIBLE);

                // Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
            }
            break;
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

    public void setBackStack(String value) {
        backStack = value;
    }

    public void closeImageHandler() {
        searchBar.setText("");
        host.setVisibility(View.INVISIBLE);
        ShopManager_layout.setVisibility(View.INVISIBLE);
        Maps_layout.setVisibility(View.INVISIBLE);
        main_fragment_holder.setVisibility(View.INVISIBLE);
        Notification_layout.setVisibility(View.INVISIBLE);
        compareLayout.setVisibility(View.INVISIBLE);
        floatingButton.setVisibility(View.INVISIBLE);

        if (backStack.equals("user")) {
            host.setVisibility(View.VISIBLE);
            SetImage();

        } else if (backStack.equals("shop")) {
            ShopManager_layout.setVisibility(View.VISIBLE);
            SetImage();
        } else if (backStack.equals("product")) {
            ShopManager_layout.setVisibility(View.VISIBLE);
        }


    }

    public void openImageHandler(String tableName, String id) {
        ImageHandler imageHandler = new ImageHandler();


        Bundle bundle1 = new Bundle();
        bundle1.putString("ID", id);
        bundle1.putString("TableName", tableName);


        imageHandler.setArguments(bundle1);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Main_Fragment_Holder, imageHandler);
        fragmentTransaction.commit();

        searchBar.setText("");
        host.setVisibility(View.INVISIBLE);
        ShopManager_layout.setVisibility(View.INVISIBLE);
        Maps_layout.setVisibility(View.INVISIBLE);
        main_fragment_holder.setVisibility(View.VISIBLE);
        Notification_layout.setVisibility(View.INVISIBLE);
        compareLayout.setVisibility(View.INVISIBLE);
        floatingButton.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addShop) {


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_manage) {
            searchBar.setText("");
            ActiveUserDetail.getCustomInstance(getApplicationContext()).logoutUser();
            Intent o = new Intent(MainPage.this, LoginActivity.class);
            startActivity(o);
            finish();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_share) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_netStatus) {
            if (checkInternet.getCustomInstance(getApplicationContext()).isConnected()) {

                db.getDBStatus(popPup, R.id.count, ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastProductID(), ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastShopID(), ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCategoryID(), ActiveUserDetail.getCustomInstance(getApplicationContext()).getLastCustomPID());

            } else {
                Blurry.with(getApplicationContext()).sampling(2).onto((ViewGroup) mainPage.getRootView());

                if (dialog == null) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainPage.this);

                    alert.setTitle("Oops !!");
                    alert.setMessage("Network Connection Required To Update");
                    alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Blurry.delete((ViewGroup) mainPage.getRootView());
                        }
                    });
                    dialog = alert.create();
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Blurry.delete((ViewGroup) mainPage.getRootView());
                        dialog.dismiss();
                    }
                }, 4000);
            }

        }


        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    public void showroute(Double latitude, Double longitude) {
        Location location = tracker.getLocation();
        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng destination = new LatLng(latitude, longitude);
        DrawRouteMaps.getInstance(this)
                .draw(origin, destination, mMap);
        DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.ic_place_black_24dp, "Origin Location");
        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.ic_person_pin_circle_black_24dp, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
    }
}
