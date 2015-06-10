package com.example.amado.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Amado on 22/05/2015.
 */
public class CrimeCamaraFragment extends Fragment{
    private static final String TAG = "CrimeCamaraFragment";
    private View mProgressContainer;
    private android.hardware.Camera mCamera;
    private SurfaceView mSurfaceView;
    private int mOrientation;
    public static final String EXTRA_PHOTO_FILENAME =
            "com.example.amado.criminalintent.photo_filename";
    public static final String EXTRA_ORIENTATION = "orientation";


    private android.hardware.Camera.ShutterCallback mShutterCallback = new android.hardware.Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private android.hardware.Camera.PictureCallback mJpegCallback = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            String fileName = UUID.randomUUID().toString()+".jpg";
            FileOutputStream os = null;
            boolean sucess =true;
            try {
                os= getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                os.write(data);

            }catch (Exception e){
                Log.e(TAG, "Error writing to file "+ fileName, e);
                sucess = false;
            }finally {
                try{
                    if (os != null) os.close();
                }catch (Exception e){
                    Log.e(TAG, "Error closing file "+fileName, e);
                    sucess= false;
                }
            }
            if(sucess){
               Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, fileName);
                i.putExtra(EXTRA_ORIENTATION, mOrientation);
                getActivity().setResult(Activity.RESULT_OK, i);
            }else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };

    @Nullable
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_camara, container, false);

        mProgressContainer = v.findViewById(R.id.crime_camara_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        Button takePictureButton = (Button)v.findViewById(R.id.crime_camara_takePicturebutton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mCamera != null){
                   mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                   Display display = ((WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE))
                           .getDefaultDisplay();
                   mOrientation = display.getRotation();

               }
            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camara_surface);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(mCamera != null){
                        mCamera.setPreviewDisplay(holder);
                    }
                }catch (IOException exception){
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(mCamera==null) return;
                android.hardware.Camera.Parameters parameters = mCamera.getParameters();
                android.hardware.Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(),
                width, height);
                parameters.setPreviewSize(s.width, s.height);
                s= getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
                parameters.setPictureSize(s.width, s.height);


                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                }catch (Exception e){
                    Log.e(TAG , "Could no start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(mCamera != null){
                    mCamera.stopPreview();
                }
            }
        });

        return v;

    }
    @SuppressWarnings("deprecation")
    @TargetApi(9)
    @Override
    public void onResume() {

        super.onResume();
       try{
            checkAndReleaseCamara();
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD){
                mCamera = android.hardware.Camera.open(0);
            }
            mCamera = android.hardware.Camera.open();
        }catch (Exception e){
            Log.e(TAG , "Error opening camera");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        checkAndReleaseCamara();
    }

    private void checkAndReleaseCamara() {
        if(mCamera != null){
            mCamera.startPreview();
            mCamera.release();
            mCamera= null;
        }
    }

    private android.hardware.Camera.Size getBestSupportedSize(List<android.hardware.Camera.Size> sizes, int width, int height){
        android.hardware.Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width* bestSize.height;
        for(android.hardware.Camera.Size s: sizes){
            int area = s.width*s.height;
            if(area>largestArea){
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }
}
