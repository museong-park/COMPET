package com.mu.compet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mu.compet.data.Post;

import java.util.Random;


public class HomeFragment extends Fragment {

    ListView listView;
    PostAdapter mAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initToolBar(getString(R.string.app_name), view);
        listView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new PostAdapter();
        listView.setAdapter(mAdapter);

        initData();
        return view;
    }

    private void initToolBar(String title, View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        TextView titleText = (TextView) view.findViewById(R.id.toolbar_title);
        titleText.setText(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    int[] resIds = {R.drawable.image_sample_post};

    String sampleString = "this is sample content ";

    private void initData() {
        StringBuilder sampleStringBuilder = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                sampleStringBuilder.append(sampleString);
            }
            Post p = new Post();
            p.setCommetCount("+3");
            p.setDate("2016년 " + "10월 " + r.nextInt(30) + "일");
            p.setProfileImage(ContextCompat.getDrawable(getContext(), R.drawable.image_default_profile));
            p.setNickName("name " + i);
            p.setPostContent(sampleStringBuilder.toString() + i);
            p.setImageCount("+2");
            p.setPostImage(ContextCompat.getDrawable(getContext(), resIds[i % resIds.length]));
            mAdapter.add(p);
        }
    }

}