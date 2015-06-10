package com.example.amado.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Amado on 09/06/2015.
 */
public class ImageFragment extends DialogFragment {
    private static final String TAG ="ImageFragment";
    public static final String EXTRA_IMAGE_PATH = "com.example.amada.criminalintent.image_path";
    public static final String EXTRA_ORIENTATION = "orientation";
    private ImageView mImageView;
    private int mOrientation;

    public static ImageFragment newInstance(String imagePath, int orientation){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        args.putInt(EXTRA_ORIENTATION, orientation);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        mOrientation = (int)getArguments().getInt(EXTRA_ORIENTATION);
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);

        mImageView.setImageDrawable(image);
        if(mOrientation == Surface.ROTATION_90&& Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB) {
            mImageView.setRotation(90);
            Log.d(TAG, "orientation portrait");
        }

        return mImageView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }
}
