package com.nerdspoint.android.chandigarh.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageHandler extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {




    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;

    LinearLayout layout;

    private Bitmap bitmap=null;

    private Button cancle,choose,upload;

    private ImageView imageView;

    String fileName = null;
    Context context;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="https://baljeet808singh.000webhostapp.com/chandigarh/UploadImage.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private String id,tableName="";

    private View view;

    public ImageHandler()
    {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_image_handler, container, false);
        layout = (LinearLayout) view.findViewById(R.id.camera_layout);



        cancle = (Button) view.findViewById(R.id.cancle_bn);
        upload = (Button) view.findViewById(R.id.upload);
        choose = (Button) view.findViewById(R.id.choose);

        cancle.setOnClickListener(this);
        choose.setOnClickListener(this);
        upload.setOnClickListener(this);

        imageView =(ImageView) view.findViewById(R.id.photu);

        imageView.setOnClickListener(this);

        Bundle bundle = getArguments();
        try {

            id = bundle.getString("ID");
            tableName = bundle.getString("TableName");
        }
        catch(Exception e)
        {
            Log.d("error "," while fetching parameters in IMAGEHANDLER >>"+e.getMessage());
        }
       // onClick(view.findViewById(R.id.choose));

        return  view;
    }

    public void setUpCamera()
    {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) view.findViewById(R.id.camerapreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        controlInflater = LayoutInflater.from(getActivity().getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.custom, null);
        LinearLayout.LayoutParams layoutParamsControl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        getActivity().addContentView(viewControl, layoutParamsControl);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(previewing){
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

    public void setContext(Context  context)
    {
        this.context= context;
    }



    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private void uploadImage(){
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        if(s.equals("Successfully Uploaded")) {
                            ((MainPage)getActivity()).closeImageHandler();
                            ActiveUserDetail.getCustomInstance(getActivity()).setUserImageName(fileName.toString()+".jpeg");
                            Toast.makeText(getActivity(), "success from android " + s, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.d("ERROR ","\n\n\n\n while fetching parameters in IMAGEHANDLER >>"+s.toString()+"\n\n\n\n\n\n\n");
                            Toast.makeText(getActivity(), "Network error, Please try again "+s, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();

                        Toast.makeText(getActivity(), ""+volleyError, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = fileName.toString();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                params.put("tableName",tableName);
                params.put("id",id);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            fileName = filePath.getLastPathSegment();
            Toast.makeText(getActivity(), "fileName  fetched > "+fileName, Toast.LENGTH_SHORT).show();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Please choose a image first", Toast.LENGTH_SHORT).show();
        }
    }








    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.choose :
            {
                showFileChooser();
            }break;
            case R.id.cancel :
            {
                ((MainPage)getActivity()).closeImageHandler();
            }break;
            case R.id.photu :
            {
                setUpCamera();
            }break;

            case R.id.upload :
            {
                if(bitmap==null || fileName==null)
                {
                    Toast.makeText(getActivity(), "Please select a image first", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImage();
                }
            }break;
        }
    }
}
