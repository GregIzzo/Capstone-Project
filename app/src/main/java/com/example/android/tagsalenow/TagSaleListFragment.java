package com.example.android.tagsalenow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TagSaleListFragment extends Fragment implements TagSaleListRecyclerAdapter.TagSaleListAdapterOnClickHandler {

    private static final String TAG = "GGG";
    private RecyclerView recyclerView;
    private TagSaleListRecyclerAdapter tagSaleListRecyclerAdapter;

    public TagSaleListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tagsalelist, container, false);

        recyclerView = rootView.findViewById(R.id.rv_tagsalelist);
        assert recyclerView != null;
        Log.d(TAG, "onCreateView: ZZZZ recyclerview=" + recyclerView.getId());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        if (mLayoutManager == null) Log.d(TAG, "onCreate: LAYOUTMANAGER IS NULL");
        recyclerView.setLayoutManager(mLayoutManager);
        tagSaleListRecyclerAdapter = new TagSaleListRecyclerAdapter(this);
        recyclerView.setAdapter(tagSaleListRecyclerAdapter);
        tagSaleListRecyclerAdapter.dataChanged();
          return rootView;
    }

    @Override
    public void onClick(int listPosition) {

    }
}
