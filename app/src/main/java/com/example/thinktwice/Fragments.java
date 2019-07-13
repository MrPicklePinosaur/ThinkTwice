package com.example.thinktwice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

class FragmentHome extends Fragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {
        return inflater.inflate(R.layout.fragment_home,null);
    }
}

class FragmentCamera extends Fragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStats) {
        return inflater.inflate(R.layout.fragment_camera,null);
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
