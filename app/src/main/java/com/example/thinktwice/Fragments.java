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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


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

        /* ML stuff not needed for MVP
        String result = Detection.labelGuesser(bmp);
        System.out.println(result); */

        //Once we get the result, a panel slides up and shows info about the item
        //show_item_info();

    }

    public void show_item_info() { //slides a new fragment in that shows the details of the item scanned
        //this block of code is from https://codinginflow.com/tutorials/android/fragment-animation-interface
        FragmentCameraScanned scanned_fragment = new FragmentCameraScanned();
        FragmentTransaction trans = Global.manager.beginTransaction();
        trans.setCustomAnimations(R.anim.slide_in_fragment,R.anim.slide_out_fragment,R.anim.slide_in_fragment,R.anim.slide_out_fragment);

        trans.addToBackStack(null);
        trans.add(R.id.fragment_scanner_container,scanned_fragment,"SCANNED_FRAGMENT").commit();

    }

}

class FragmentCameraScanned extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {

        View view = inflater.inflate(R.layout.fragment_camera_scanner,null);

        return view;
    }

}

class FragmentCommunity extends Fragment implements View.OnClickListener {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {
        View view = inflater.inflate(R.layout.fragment_community,null);

        //Buttons
        Button send_http_button = view.findViewById(R.id.send_http_button);
        send_http_button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_http_button:
                WebScraper.sendRequest("hello world");
                break;
        }
    }
}

class FragmentExplore extends Fragment implements View.OnClickListener {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {
        View view = inflater.inflate(R.layout.fragment_explore,null);

        //Buttons
        Button web_scrape_button = view.findViewById(R.id.web_scrape_button);
        web_scrape_button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.web_scrape_button:
                WebScraper.getLinks("https://en.wikipedia.org/wiki/List_of_programming_languages");
                break;
        }
    }
}
