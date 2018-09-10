package com.example.shereen.picturesearch.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.example.shereen.picturesearch.GlideApp;
import com.example.shereen.picturesearch.R;
import com.example.shereen.picturesearch.recycler.ImageAdapter;
import com.example.shereen.picturesearch.recycler.OnBottomReachedListener;
import com.example.shereen.picturesearch.recycler.RecyclerItemClickListener;
import com.example.shereen.picturesearch.entity.SingleImage;
import com.example.shereen.picturesearch.gson.TopLevel;
import com.example.shereen.picturesearch.tasks.ImageArrayMaker;

import java.util.ArrayList;
import java.util.List;

public class PicturesFragment extends Fragment {

    View rootView;

    private OnPictureFragmentInteractionListener mListener;
    List<SingleImage> imageUrls = new ArrayList<>();
    List<SingleImage> imageUrlsMain = new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    ImageArrayMaker imageArrayMaker;

    int picturesPerPage = 66;
    int counter = 1;


    public PicturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pictures, container, false);



//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.placeholder);
//        requestOptions.error(R.drawable.imagenotfound);

        imageAdapter = new ImageAdapter(Glide.with(this), imageUrls);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        recyclerView.setAdapter(imageAdapter);

        imageArrayMaker = new ImageArrayMaker();

        System.out.println("Called again!");

        if (mListener != null) {
            mListener.onFragmentInteraction("send toplevel");
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("Short click "+position);
                        if (mListener != null) {
                            mListener.startProgressBar();
                            mListener.startDownloadFragment(imageUrls.get(position));
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        System.out.println("Long click "+position);
                        if (mListener != null) {
                            mListener.startProgressBar();
                            mListener.startDownloadFragment(imageUrls.get(position));
                        }
                    }
                })
        );

        imageAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                System.out.println("REACHED BOTTOM!!!");
                if(++counter<6){
                    System.out.println(counter + " not exceeded");
                }else{
                    //ask if want more
                    System.out.println(counter + " exceeded");
                }

                if (mListener != null) {
                    mListener.endReached(counter);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPictureFragmentInteractionListener) {
            mListener = (OnPictureFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPictureFragmentInteractionListener");
        }
    }

    public void initialSendToFragment(TopLevel topLevel){

        imageUrlsMain = imageArrayMaker.makeImageArrayList(topLevel);
        System.out.println(imageUrlsMain.get(0));

        //load into pictures 0-29 into displaying array
        loadArray(0,picturesPerPage);
        //spinner.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.stopProgressBar();
        }
    }


    public void loadArray(int start, int end){

        for(int i=start;i<end;i++){
            imageUrls.add(imageUrlsMain.get(i));
        }
        imageAdapter.notifyDataSetChanged();
    }

    public void addToArray(){

        System.out.println("Counter: "+counter);
        switch(counter) {
            case 1:
                loadArray(0, picturesPerPage);
                break;
            case 2:
                loadArray(picturesPerPage, picturesPerPage * 2);
                break;
            case 3:
                loadArray(picturesPerPage * 2, picturesPerPage * 3);
                break;
            case 4:
                loadArray(picturesPerPage * 3, picturesPerPage * 4);
                break;
            case 5:
                loadArray(picturesPerPage * 4, picturesPerPage * 5);
                break;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPictureFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String message);
        void endReached(int count);
        void arrayEmpty();
        void stopProgressBar();
        void startProgressBar();
        void startDownloadFragment(SingleImage image);
    }

}
