package com.nerdspoint.android.chandigarh.offlineDB;

/**
 * Created by android on 11-03-2017.
 */


import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.logging.LogRecord;

import static android.net.sip.SipErrorCode.TIME_OUT;


public class DBHandler extends SQLiteOpenHelper
{


    Context context;
    int progress=0;
    String count;


    String[] colNames = {"ShopID", "UID", "ShopName", "ShopAddress", "PinCode", "Sector", "SCO", "Latitude", "Longitude", "CategoryID"};
    String[] colNames1 = {"ProductID", "ProductName", "CategoryID", "Price"};
    String[] colNames2 = {"CategoryID", "CategoryName"};
    String[] colNames3 = {"CustomPID","ProductID", "CategoryID", "ShopID", "ProductName", "Price", "IsActive"};


    private String update_url = "/offlineUpdate.php";


    private String count_url ="/CountNewData.php";



    public DBHandler(Context context) {
        super(context, "OfflineDB.db", null, 1);
        this.context = context;
        update_url=ipAddress.getCustomInstance(context).getIp()+update_url;
        count_url=ipAddress.getCustomInstance(context).getIp()+count_url;
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
                    count="not running";
                    TextView textView= (TextView) poppup.findViewById(id);
                    textView.setText(count);
                }else
                {
                    count=(response);
                    if(Integer.parseInt(response)>5) {
                        TextView textView = (TextView) poppup.findViewById(id);
                        textView.setText(count);
                    }
                    else {
                        // poppup.setVisibility(View.GONE);
                        TextView textView = (TextView) poppup.findViewById(id);
                        textView.setText("Updated");
                        //   Toast.makeText(context,"already updated",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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




    public void syncOffline()
    {

        if(ActiveUserDetail.getCustomInstance(context).getIsFirstSync()) {
            CreateTable("ShopMasterTable", colNames, 10);
            CreateTable("Product", colNames1, 4);
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






    public void updateOfflineTable(final String TableName,final String[] colNames,final String ColoumName,final int Value)
    {
        StringRequest request = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {



                Log.d("Response", response);
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
                    //  Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                map.put("tableName",TableName);
                map.put("value",Value+"");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }






    public Cursor getAll(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("select * from "+tableName+"", null);
    }





    public void updateLastIds()
    {
        SQLiteDatabase db1 = this.getReadableDatabase();


        Cursor cursor2= db1.rawQuery("Select MAX(ShopID) from ShopMasterTable",null);
        cursor2.moveToFirst();

        String id = cursor2.getString(0);
        Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastShopID(Integer.parseInt(id));

        Cursor cursor3= db1.rawQuery("select MAX(ProductID) from Product", null);

        cursor3.moveToFirst();
        String id3 = cursor3.getString(0);
        Toast.makeText(context, ""+id3, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastProductID(Integer.parseInt(id3));

        Cursor cursor4= db1.rawQuery("select MAX(CategoryID) from Category", null);

        cursor4.moveToFirst();
        String id4 = cursor4.getString(0);
        Toast.makeText(context, ""+id4, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastCateegoryID(Integer.parseInt(id4));

        Cursor cursor5= db1.rawQuery("select MAX(CustomPID) from CustomProductDetail", null);

        cursor5.moveToFirst();
        String id5 = cursor5.getString(0);
        Toast.makeText(context, ""+id5, Toast.LENGTH_SHORT).show();
        ActiveUserDetail.getCustomInstance(context).setLastCustomPID(Integer.parseInt(id5));

        Toast.makeText(context,"last shopid "+ActiveUserDetail.getCustomInstance(context).getLastShopID()+" Last ProductID "+ActiveUserDetail.getCustomInstance(context).getLastProductID()+" Category id "+ActiveUserDetail.getCustomInstance(context).getLastCategoryID()+" customPID "+ActiveUserDetail.getCustomInstance(context).getLastCustomPID()+" ",Toast.LENGTH_LONG).show();
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
                sql=sql+""+coloms[i]+" text);";

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
        // Toast.makeText(context,"created "+tableName,Toast.LENGTH_SHORT).show();

        db.close();

    }

    public ContentValues get(long id,String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        ContentValues row = new ContentValues();
        Cursor cur = db.rawQuery("select title, priority from todos where _id = ?", new String[] { String.valueOf(id) });
        if (cur.moveToNext()) {
            row.put("title", cur.getString(0));
            row.put("priority", cur.getInt(1));
        }
        cur.close();
        db.close();
        return row;
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
}
