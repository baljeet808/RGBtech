package com.nerdspoint.android.chandigarh.offlineDB;

/**
 * Created by android on 11-03-2017.
 */


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
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


public class DBHandler {
    private SQLiteOpenHelper _openHelper;
    Context context;

    private String update_url = "https://baljeet808singh.000webhostapp.com/chandigarh/offlineUpdate.php";

    /**
     * Construct a new database helper object
     * @param context The current context for the application or activity
     */
    public DBHandler(Context context) {
        _openHelper = new SimpleSQLiteOpenHelper(context);
        this.context = context;


    }

    /**
     * This is an internal class that handles the creation of all database tables
     */
    class SimpleSQLiteOpenHelper extends SQLiteOpenHelper {
        SimpleSQLiteOpenHelper(Context context) {

            super(context, "OfflineDB.db", null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void syncOffline()
    {
        String[] colNames = {"ShopID", "UID", "ShopName", "ShopAddress", "PinCode", "Sector", "SCO", "Latitude", "Longitude", "CategoryID"};
        String[] colNames1 = {"ProductID", "ProductName", "CategoryID", "Price"};
        String[] colNames2 = {"CategoryID", "CategoryName"};
        String[] colNames3 = {"ProductID", "CategoryID", "ShopID", "ProductName", "Price", "IsActive"};


        if(ActiveUserDetail.getCustomInstance(context).getIsFirstSync()) {
                CreateTable("ShopMasterTable", colNames, 10);
                CreateTable("Product", colNames1, 4);
                CreateTable("Category", colNames2, 2);
                CreateTable("CustomProductDetail", colNames3, 6);
                ActiveUserDetail.getCustomInstance(context).setIsFirstSync(false);
            }
            updateOfflineTable("ShopMasterTable",colNames,"ShopID",ActiveUserDetail.getCustomInstance(context).getLastShopID());
            updateOfflineTable("Product",colNames1,"ProductID",ActiveUserDetail.getCustomInstance(context).getLastProductID());
            updateOfflineTable("Category",colNames2,"CategoryID",ActiveUserDetail.getCustomInstance(context).getLastCategoryID());
            updateOfflineTable("CustomProductDetail",colNames3,"ProductID",ActiveUserDetail.getCustomInstance(context).getLastProductID());

    }


    public void updateOfflineTable(final String TableName,final String[] colNames,final String ColoumName,final int Value)
    {
        StringRequest request = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                Log.d("Response", response);

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
                        if(j==(jsonArray.length()-1))
                        {
                            if(jsonObject.get("ShopID")!=null)
                            {
                                ActiveUserDetail.getCustomInstance(context).setLastShopID(jsonObject.getInt("ShopID"));
                            }
                            else if(jsonObject.get("ProductID")!=null)
                            {
                                ActiveUserDetail.getCustomInstance(context).setLastProductID(jsonObject.getInt("ProductID"));
                            }
                            else if(jsonObject.get("CategoryID")!=null)
                            {
                                ActiveUserDetail.getCustomInstance(context).setLastCateegoryID(jsonObject.getInt("CategoryID"));
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap() ;
                map.put("tableName",TableName);
                map.put("coloumName",ColoumName);
                map.put("value",Value);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }
    /**
     * Return a cursor object with all rows in the table.
     * @return A cursor suitable for use in a SimpleCursorAdapter
     */
    public Cursor getAll(String tableName) {
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("select * from "+tableName+"", null);
    }

    /**
     * Return values for a single row with the specified id
    // * @param id The unique id for the row o fetch
     * @return All column values are stored as properties in the ContentValues object
     */

    public void CreateTable(String tableName,String[] coloms,int colCount)
    {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        if (db == null) {
            return;
        }
        String sql = "CREATE TABLE "+tableName+ "(";
        for(int i=0 ;i<colCount;i++ )
        {
            if(i==(colCount-1))
            {
                sql=sql+""+coloms[i]+" text);";

            }else
                {
                sql = sql + "" + coloms[i] + " text, ";
            }
        }

        Log.i("haiyang:createDB=", sql);
        db.execSQL(sql);
        db.close();

    }

    public ContentValues get(long id,String tableName) {
        SQLiteDatabase db = _openHelper.getReadableDatabase();
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
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        if (db == null) {
            return 0;
        }
        long id = db.insert(TableName, null, row);
        db.close();
        return id;
    }

}
