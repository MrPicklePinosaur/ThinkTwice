package com.example.thinktwice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

class FragmentHome extends Fragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {
        return inflater.inflate(R.layout.fragment_home,null);
    }
}

class FragmentCamera extends Fragment implements View.OnClickListener {

    ImageView snapshot;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {

        View view = inflater.inflate(R.layout.fragment_camera,null);

        //Buttons
        Button take_photo_button = view.findViewById(R.id.take_photo_button);
        take_photo_button.setOnClickListener(this);

        //images
        snapshot = view.findViewById(R.id.snapshot); //shows a snapshot of the image that was just taken

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_photo_button:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        snapshot.setImageBitmap(bmp);
        Detection.labelGuesser(bmp);

        /* Possibly delete, as we actually still need the bitmap later
        //attempt to dispose the bitmap
        if (bmp != null && bmp.isRecycled() == false) {
            bmp.recycle();
            bmp = null;
        }
        */
    }
}

class FragmentCommunity extends Fragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {
        return inflater.inflate(R.layout.fragment_community,null);
    }
}

class FragmentExplore extends Fragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {
        return inflater.inflate(R.layout.fragment_explore,null);
    }
}
