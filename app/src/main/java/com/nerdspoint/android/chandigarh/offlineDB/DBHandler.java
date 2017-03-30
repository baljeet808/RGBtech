package com.nerdspoint.android.chandigarh.offlineDB;

/**
 * Created by android on 11-03-2017.
 */


import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.populateSearchArray;
import com.nerdspoint.android.chandigarh.adapters.quickSearchAdapter;
import com.nerdspoint.android.chandigarh.fragments.QuickSearchResults;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.logging.LogRecord;

import static android.net.sip.SipErrorCode.TIME_OUT;


public class DBHandler extends SQLiteOpenHelper
{

    String VendorFname,VendorLname,VendorCnumber,UID;

    int progress=0;
    String count;

    AutoCompleteTextView searchBar;

    View userProfile;

    Context context;



    String[] colNames = {"ShopID", "UID", "ShopContactNo", "ShopName", "ShopAddress", "PinCode", "Sector", "SCO", "Latitude", "Longitude", "CategoryID"};
    String[] colNames1 = {"ProductID", "ProductName", "CategoryID"};
    String[] colNames2 = {"CategoryID", "CategoryName"};
    String[] colNames3 = {"CustomPID","ProductID", "CategoryID", "ShopID", "ProductName", "Price", "IsActive"};


    private String update_url = "/offlineUpdate.php";
    private String vendorProfile_url = "/ballu.php";


    private String count_url ="/CountNewData.php";



    public DBHandler(Context context) {
        super(context, "OfflineDB.db", null, 1);
        this.context = context;
        update_url=ipAddress.getCustomInstance(context).getIp()+update_url;
        count_url=ipAddress.getCustomInstance(context).getIp()+count_url;
        vendorProfile_url=ipAddress.getCustomInstance(context).getIp()+vendorProfile_url;

    }






    public String getCount()
    {
        return count;
    }





    public void getDBStatus(final RelativeLayout poppup, final int id, final int PValue, final int SValue, final int CValue,final int CPValue)
    {
        StringRequest request = new StringRequest(Request.Method.POST, count_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                count="0";
                Log.d("Response", response);

                if(response.contains("fail") || response.contains("<br") )
                {
                    count="!";
                    TextView textView= (TextView) poppup.findViewById(id);
                    textView.setText(count);
                }else
                {
                    count=(response);
                    if(Integer.parseInt(response)>0) {
                        TextView textView = (TextView) poppup.findViewById(id);
                        textView.setText(count);
                        poppup.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "values to update "+response, Toast.LENGTH_SHORT).show();
                    }
                    else {
                         poppup.setVisibility(View.GONE);
                        TextView textView = (TextView) poppup.findViewById(id);
                        textView.setText("0");
                           Toast.makeText(context,"already updated",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                  Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                Log.d("ERROR","error => "+error.toString());
            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>() ;
                map.put("PValue",""+PValue);
                map.put("CValue",""+CValue);
                map.put("SValue",""+SValue);
                map.put("CPValue",""+CPValue);

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);


    }






    public int checkProgress()
    {
        return progress;
    }




    public void syncOffline(AutoCompleteTextView searchBar)
    {

    //    Toast.makeText(context, "sync started", Toast.LENGTH_SHORT).show();
        this.searchBar= searchBar;



        if(ActiveUserDetail.getCustomInstance(context).getIsFirstSync()) {
            CreateTable("ShopMasterTable", colNames, 11);
            CreateTable("Product", colNames1, 3);
            CreateTable("Category", colNames2, 2);
            CreateTable("CustomProductDetail", colNames3, 7);
            ActiveUserDetail.getCustomInstance(context).setIsFirstSync(false);
        }
        updateOfflineTable("ShopMasterTable",colNames,"ShopID",ActiveUserDetail.getCustomInstance(context).getLastShopID());

    }


    public void UpdateProductTable()
    {
        updateOfflineTable("Product",colNames1,"ProductID",ActiveUserDetail.getCustomInstance(context).getLastProductID());

    }

    public void UpdateCategoryTable()
    {
        updateOfflineTable("Category",colNames2,"CategoryID",ActiveUserDetail.getCustomInstance(context).getLastCategoryID());

    }

    public void UpdateCustomProductDetailTable()
    {
        updateOfflineTable("CustomProductDetail",colNames3,"CustomPID",ActiveUserDetail.getCustomInstance(context).getLastCustomPID());

    }


    public void getVendorProfile(final String ShopId)
    {

        StringRequest request = new StringRequest(Request.Method.POST, vendorProfile_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {



                // Log.d("Response", response);
                Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();


                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    VendorFname= jsonObject.getString("FirstName");
                    VendorLname = jsonObject.getString("LastName");
                    VendorCnumber = jsonObject.getString("PhoneNumber");
                    UID = jsonObject.getString("UID");
                    setVendorProfile();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "catch showing "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"on response error -"+error.toString(), Toast.LENGTH_LONG).show();
                //  Log.d("ERROR","error => "+error.toString());
            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>() ;
                map.put("ShopID",ShopId);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    public void setVendorProfile()
    {
        TextView ownerFname = (TextView) userProfile.findViewById(R.id.owner_Fname);
        TextView ownerLname = (TextView) userProfile.findViewById(R.id.owner_Sname);
        final TextView Contactt = (TextView) userProfile.findViewById(R.id.owner_contactno);
        Contactt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(Contactt.getText().toString()));
                    context.startActivity(intent);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "call failed because "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        ownerFname.setText(VendorFname);
        ownerLname.setText(VendorLname);
        Contactt.setText(VendorCnumber);

        ListView shopsOwned = (ListView) userProfile.findViewById(R.id.shop_owned);

        List<String> shops;
        ArrayAdapter adapter;

        shops = new ArrayList<>();
        Cursor cursor = getOwnedShops(UID);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                shops.add(cursor.getString(cursor.getColumnIndex("ShopName")));
                cursor.moveToNext();
            }
        }
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, shops);

        shopsOwned.setAdapter(adapter);
        shopsOwned.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateOfflineTable(final String TableName,final String[] colNames,final String ColoumName,final int Value)
    {
        StringRequest request = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {



               // Log.d("Response", response);
               Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();


                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for(int j=0;j<jsonArray.length();j++)
                    {

                        JSONObject jsonObject= jsonArray.getJSONObject(j);
                        ContentValues row = new ContentValues();
                        for(int k =0;k<colNames.length;k++)
                        {
                            row.put(colNames[k],jsonObject.get(colNames[k]).toString());
                        }
                        add(TableName,row);

                    }

                    progress=progress+25;
                 Toast.makeText(context, ""+progress, Toast.LENGTH_SHORT).show();
                    // updateLastIds();
                    if(TableName.equals("ShopMasterTable"))
                    {
                        UpdateProductTable();
                    }
                    else if(TableName.equals("Product"))
                    {
                        UpdateCategoryTable();
                    }else  if(TableName.equals("Category"))
                    {
                        UpdateCustomProductDetailTable();
                    }else if(TableName.equals("CustomProductDetail"))
                    {
                        Toast.makeText(context,"tables saved ",Toast.LENGTH_SHORT).show();
                        updateLastIds();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progress=progress+25;
                     Toast.makeText(context, "catch showing "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(context,"on response error -"+error.toString(), Toast.LENGTH_LONG).show();
              //  Log.d("ERROR","error => "+error.toString());
            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>() ;
                map.put("tableName",TableName);
                map.put("value",Value+"");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    public int getShopCount(String ProductID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db== null)
        {
            return 0;
        }
        Cursor cursor=db.rawQuery("Select ShopID from CustomProductDetail where ProductID = "+ProductID,null);
        return cursor.getCount();
    }


    public Cursor getShop(String shopName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db== null)
        {
            return null;
        }
        return db.rawQuery("select ShopID, ShopName, ShopAddress, UID, Sector, SCO , ShopContactNo from ShopMasterTable where ShopName LIKE '%"+shopName+"%'",null);
    }


    public Cursor getProductByCategoryName(String Category)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db== null)
        {
            return null;
        }
        return db.rawQuery("select ProductName FROM Product Where CategoryID = (Select CategoryID FROM Category Where CategoryName = '"+Category+"')",null);
    }

    public Cursor getShopByID(String shopId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db== null)
        {
            return null;
        }
        return db.rawQuery("select ShopName, ShopAddress, UID, Sector, SCO , ShopContactNo , CategoryID from ShopMasterTable where ShopID = "+shopId+"",null);
    }

    public Cursor getCategory(String CategoryName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db== null)
        {
            return null;
        }
        return db.rawQuery("SELECT ProductID, ProductName, CategoryID from Product where CategoryID = (SELECT CategoryID FROM Category WHERE CategoryName = '"+CategoryName+"')",null);
    }

    public Cursor getProduct (String productName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db==null)
        {
            return null;
        }
        return db.rawQuery("select CustomPID, ShopID, CategoryID, Price, ProductName, ProductID FROM CustomProductDetail WHERE ProductID = (Select ProductID FROM Product WHERE ProductName= '"+productName+"')",null);
    }

    public Cursor getCategoryID(String categoryName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db== null)
        {
            return null;
        }
        return db.rawQuery("select CategoryID from Category where CategoryName = '"+categoryName+"'",null);
    }

    public Cursor getCategoryName (String categoryID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db== null)
        {
            return null;
        }
        return db.rawQuery("select CategoryName from Category where CategoryID = '"+categoryID+"'",null);

    }


    public Cursor getAll(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("select * from "+tableName+"", null);
    }

    public Cursor getOwnedShops(String UID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("Select ShopName from ShopMasterTable where UID = "+UID+"", null);
    }





    public void updateLastIds()
    {
        SQLiteDatabase db1 = this.getReadableDatabase();


        Cursor cursor2= db1.rawQuery("Select MAX(ShopID) from ShopMasterTable",null);
        cursor2.moveToFirst();

        String id = cursor2.getString(0);
     //   Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastShopID(Integer.parseInt(id));

        Cursor cursor3= db1.rawQuery("select MAX(ProductID) from Product", null);

        cursor3.moveToFirst();
        String id3 = cursor3.getString(0);
     //  Toast.makeText(context, ""+id3, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastProductID(Integer.parseInt(id3));

        Cursor cursor4= db1.rawQuery("select MAX(CategoryID) from Category", null);

        cursor4.moveToFirst();
        String id4 = cursor4.getString(0);
      //  Toast.makeText(context, ""+id4, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastCateegoryID(Integer.parseInt(id4));

        Cursor cursor5= db1.rawQuery("select MAX(CustomPID) from CustomProductDetail", null);

        cursor5.moveToFirst();
        String id5 = cursor5.getString(0);
      //  Toast.makeText(context, ""+id5, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastCustomPID(Integer.parseInt(id5));

       Toast.makeText(context,"last shopid "+ActiveUserDetail.getCustomInstance(context).getLastShopID()+" Last ProductID "+ActiveUserDetail.getCustomInstance(context).getLastProductID()+" Category id "+ActiveUserDetail.getCustomInstance(context).getLastCategoryID()+" customPID "+ActiveUserDetail.getCustomInstance(context).getLastCustomPID()+" ",Toast.LENGTH_LONG).show();
        populateSearchArray.getCustomInstance(context,searchBar).populate("Shops");
    }








    public void CreateTable(String tableName,String[] coloms,int colCount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return;
        }
        String sql = "CREATE TABLE IF NOT EXISTS "+tableName+ "(";
        for(int i=0 ;i<colCount;i++ )
        {

            if(i==(colCount-1))
            {
                if(coloms[i]!="ProductID" && coloms[i]!="CategoryID" && coloms[i]!="ShopID" && coloms[i]!="CustomPID") {
                    sql = sql + "" + coloms[i] + " text);";
                }
                else
                {
                    sql= sql+""+coloms[i]+" int);";
                }
            }else
            {

                if(coloms[i]=="ProductID")
                {
                    sql= sql+""+coloms[i]+" int, ";
                }

                else  if(coloms[i]=="CategoryID")
                {
                    sql= sql+""+coloms[i]+" int, ";
                }
                else  if(coloms[i]=="ShopID")
                {
                    sql= sql+""+coloms[i]+" int, ";
                }
                else  if(coloms[i]=="CustomPID")
                {
                    sql= sql+""+coloms[i]+" int, ";
                }
                else {
                    sql = sql + "" + coloms[i] + " text, ";
                }
            }
        }

        //  Log.i("haiyang:createDB=","\t\t\t\t\t\t\t\t"+sql);
        db.execSQL(sql);
       Toast.makeText(context,"created "+tableName,Toast.LENGTH_SHORT).show();

        db.close();

    }




    public long add(String TableName,ContentValues row) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return 0;
        }
        long id = db.insert(TableName, null, row);
        db.close();
        return id;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public View getShopOwnerDetail(String shopid,View view) {


        this.userProfile = view;
        getVendorProfile(shopid);

        return userProfile;
         }
}
