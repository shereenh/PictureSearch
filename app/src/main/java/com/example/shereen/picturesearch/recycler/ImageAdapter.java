package com.example.shereen.picturesearch.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
//import com.bumptech.glide.request.RequestOptions;
import com.example.shereen.picturesearch.R;
import com.example.shereen.picturesearch.entity.SingleImage;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private List<SingleImage> imageUrls;
    private final RequestManager glide;
    //RequestOptions requestOptions;
    OnBottomReachedListener onBottomReachedListener;


    public class MyViewHolder extends RecyclerView.ViewHolder
            {
        //public TextView titleView;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            //view.setOnClickListener(this);
            //titleView = (TextView) view.findViewById(R.id.titleView);
            imageView = (ImageView) view.findViewById(R.id.imageView);

        }
    }

    public ImageAdapter(RequestManager glide,
                        List<SingleImage> imageUrls) {
        this.imageUrls = imageUrls;
        this.glide = glide;
        //this.requestOptions = requestOptions;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_element, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SingleImage singleImage = imageUrls.get(position);

        //holder.titleView.setText(singleImage.getTitle());

       glide.load(singleImage.getImageUrl())
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.imagenotfound)
               .into(holder.imageView);

        if (position == imageUrls.size() - 1){

            onBottomReachedListener.onBottomReached(position);

        }


    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
