package com.example.shereen.picturesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.example.shereen.picturesearch.GlideApp;
import com.example.shereen.picturesearch.R;
import com.example.shereen.picturesearch.recycler.ImageAdapter;
import com.example.shereen.picturesearch.recycler.OnBottomReachedListener;
import com.example.shereen.picturesearch.recycler.RecyclerItemClickListener;
import com.example.shereen.picturesearch.entity.SingleImage;
import com.example.shereen.picturesearch.gson.TopLevel;
import com.example.shereen.picturesearch.tasks.AsyncResponse;
import com.example.shereen.picturesearch.tasks.ImageArrayMaker;

import java.util.ArrayList;
import java.util.List;

public class PicturesFragment extends Fragment
    implements AsyncResponse{

    View rootView;

    private OnPictureFragmentInteractionListener mListener;
    List<SingleImage> imageUrls = new ArrayList<>();
    List<SingleImage> imageUrlsMain = new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    ImageArrayMaker task;

    int picturesPerPage = 66; //blockSize in python code
    int counter = 0;
    int maxCounter = 0;
    int lastPage =0;


    public PicturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pictures, container, false);

        imageAdapter = new ImageAdapter(Glide.with(this), imageUrls);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        recyclerView.setAdapter(imageAdapter);

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
                counter++;
                if(mListener != null){
                    if(counter==maxCounter){
                        System.out.println(counter + " equal");
                        if(lastPage==0){
                            System.out.println("last page equals zero");
                            mListener.moreAtBottom(false);
                        }else{
                            System.out.println("last page does NOT equal zero");
                            mListener.moreAtBottom(true);
                        }
                    }
                    if(counter<maxCounter){
                        System.out.println(counter + " not exceeded");
                            mListener.moreAtBottom(true);
                    }else{
                        System.out.println(counter + " exceeded");
                            mListener.moreAtBottom(false);

                    }
                }

            }
        });

        return rootView;
    }

    void setCountSizes(int len){

        if(len > 200){

            maxCounter = len/66;  //I though 66 was a long enough length for scrolling through many pictures
            picturesPerPage = 66;

        }else if(len > 100){

            maxCounter = len/45;
            picturesPerPage = 45;

        }else{
            maxCounter = 1;
            picturesPerPage = len;

        }
        lastPage = len - maxCounter * picturesPerPage;

        System.out.println("maxCounter= "+maxCounter+"\nblockSize= "+picturesPerPage+
            "\nlastPage= "+lastPage);

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

        task = new ImageArrayMaker(topLevel,this);
        task.execute();
    }


    public void loadArray(int start, int end){

        System.out.println("Loading array from "+start+" till "+end);

        for(int i=start;i<end;i++){
            imageUrls.add(imageUrlsMain.get(i));
        }
        imageAdapter.notifyDataSetChanged();
    }

    public void addToArray(){

        System.out.println("Counter: "+counter);

        int mulCountBlock = counter*picturesPerPage;

        if(counter<maxCounter){

            loadArray(mulCountBlock,mulCountBlock+picturesPerPage);

        }else if(counter == maxCounter){
            loadArray(mulCountBlock,mulCountBlock+lastPage);

        }else{
            System.out.println("Serious error");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(task!=null){//cancel asynctask
                task.cancel(true);
        }
    }

    public interface OnPictureFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String message);
        void moreAtBottom(boolean value);
        void stopProgressBar();
        void startProgressBar();
        void startDownloadFragment(SingleImage image);
    }

    @Override
    public void processFinish(List<SingleImage> imageUrls){
        imageUrlsMain = imageUrls;

        System.out.println("length is: "+imageUrlsMain.size());
        setCountSizes(imageUrlsMain.size());
        //load into pictures 0-29 into displaying array
        loadArray(0,picturesPerPage);
        //spinner.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.stopProgressBar();
        }
    }

}
