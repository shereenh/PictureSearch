package com.example.shereen.picturesearch.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.shereen.picturesearch.R;
import com.example.shereen.picturesearch.entity.SingleImage;
import com.example.shereen.picturesearch.recycler.HistoryAdapter;
import com.example.shereen.picturesearch.recycler.ImageAdapter;
import com.example.shereen.picturesearch.recycler.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    View rootView;
    private OnHistoryFragmentInteractionListener mListener;
    List<String> historySearches = new ArrayList<>();

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;

    Button clearButton,cancelButton;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String history_content = "";

        if (getArguments() != null) {
            history_content = getArguments().getString("history");
        }

        String[] temp = history_content.split(",");
        for(String his:temp){
            historySearches.add(his);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        historyAdapter = new HistoryAdapter(historySearches);
        recyclerView = rootView.findViewById(R.id.recyclerViewHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(historyAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                System.out.println("Short click "+position);
                                if (mListener != null) {
                                    mListener.startProgressBar();
                                    mListener.searchFromHistory(historySearches.get(position));
                                }
                            }

                            @Override public void onLongItemClick(View view, int position) {
                                System.out.println("Long click "+position);
                                if (mListener != null) {
                                    mListener.startProgressBar();
                                    mListener.searchFromHistory(historySearches.get(position));
                                }
                            }
                        })
        );

        clearButton = rootView.findViewById(R.id.historyClear);
        clearButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (mListener != null) {
                    mListener.clearFromHistory();
                    historySearches.clear();
                    historySearches.add("flickr");
                    historyAdapter.notifyDataSetChanged();
                }
            }
        });

        cancelButton = rootView.findViewById(R.id.historyCancel);
        cancelButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (mListener != null) {
                    mListener.killHistoryFragment();
                }
            }
        });

        System.out.println("history searches:");
        for(String i: historySearches){
            System.out.println(i);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.searchFromHistory("hello");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryFragmentInteractionListener) {
            mListener = (OnHistoryFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHistoryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnHistoryFragmentInteractionListener {
        // TODO: Update argument type and name
        void searchFromHistory(String search);
        void clearFromHistory();
        void killHistoryFragment();
        void startProgressBar();
    }
}
