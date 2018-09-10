package com.example.shereen.picturesearch;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.shereen.picturesearch.entity.SingleImage;
import com.example.shereen.picturesearch.fragments.DownloadFragment;
import com.example.shereen.picturesearch.fragments.HistoryFragment;
import com.example.shereen.picturesearch.fragments.PicturesFragment;
import com.example.shereen.picturesearch.fragments.TextFragment;
import com.example.shereen.picturesearch.gson.TopLevel;
import com.example.shereen.picturesearch.rest.RestClient;
import com.example.shereen.picturesearch.tasks.SaveToGallery;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
        PicturesFragment.OnPictureFragmentInteractionListener, DownloadFragment.OnDownloadFragmentInteractionListener,
        HistoryFragment.OnHistoryFragmentInteractionListener{

    private Disposable disposable;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEARCH_PREFERENCES = "search_content";
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9078;
    Button moreButton,historyButton;
    ImageButton searchButton;
    EditText searchText;
    TopLevel globalTopLevel;

    PicturesFragment picturesFragment;
    TextFragment textFragment;
    DownloadFragment downloadFragment;
    HistoryFragment historyFragment;

    InputMethodManager inputManager;
    private ProgressBar spinner;

    Bitmap bitmapImage;

    SingleImage zoomImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picturesFragment = new PicturesFragment();
        textFragment = new TextFragment();
        // Start activity with introductory fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, textFragment)
                .commit();

        spinner = findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        searchButton = findViewById(R.id.searchButton);
        searchText = findViewById(R.id.searchText);
        searchButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                 //search for given input
                search();

            }
        });

        searchText.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                 //set search text empty if user clicks on it (convenience)
                searchText.setText("");

            }
        });
        //search button from keyboard
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    handled = true;
                }
                return handled;
            }
        });


        moreButton = findViewById(R.id.moreButton);
        moreButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                //initially disable button since activity has just started and user has not searched yet
                changMoreClick(false);
                //add more pictures to the bottom, user must scroll after clicking this
                if(picturesFragment!=null){
                    picturesFragment.addToArray();
                }


            }
        });

        historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                historyFragment = new HistoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("history", getSearchHistory());
                historyFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, historyFragment)
                        .commit();
            }
        });

        changMoreClick(false);

    }

    void search(){

        //hide keyboard
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        String search = searchText.getText().toString();
        print(search);

        if(search.isEmpty()){

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_search)
                    , Toast.LENGTH_LONG).show();
        }else{
            setSearchHistory(search);
            spinner.setVisibility(View.VISIBLE);
            makeRestCall(search);
        }

    }

    void changMoreClick(boolean b){

        moreButton.setClickable(b);
        moreButton.setEnabled(b);

    }


    void makeRestCall(final String searchContent){

        disposable = RestClient.getInstance()
                .getStarredRepos1(searchContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<TopLevel>() {
                            @Override
                            public void accept(TopLevel topLevel) throws Exception {
                                Log.i(TAG, "RxJava2: Response from server for toplevel ...");
//                                System.out.println("result from call: "+topLevel);
                                globalTopLevel = topLevel;
                                picturesFragment = new PicturesFragment();
                                new Thread(new Runnable() { @Override public void run() {
                                    print("Gonna destroy 1111");
                                    Glide.get(getApplicationContext()).clearDiskCache();
                                } }).start();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frameLayout, picturesFragment)
                                        .commit();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable t) throws Exception {
                                Log.i(TAG, "RxJava2, HTTP Error: " + t.getMessage());
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.not_found)+" "+searchContent
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                );

    }

    void print(String s){
        System.out.println(s);
    }



    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        Glide.get(MainActivity.this).clearMemory();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                print("Gonna destroy ");
            }
        });

        new Thread(new Runnable() { @Override public void run() {
            print("Gonna destroy 1");
        } }).start();

        super.onDestroy();
    }

    public void onFragmentInteraction(String message){
        if(message.equals("send toplevel")){
            if(picturesFragment!=null){
                picturesFragment.initialSendToFragment(globalTopLevel);
            }
        }
    }

    public void endReached(int count){

        if(count<6){
            changMoreClick(true);

        }else{
            changMoreClick(false);
        }

    }

    public void arrayEmpty(){

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_more_results)
                , Toast.LENGTH_LONG).show();

    }

    public void stopProgressBar(){
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void killDownloadFragment(){

        if(downloadFragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .remove(downloadFragment)
                    .commit();
        }
    }

    public void killHistoryFragment(){

        if(historyFragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .remove(historyFragment)
                    .commit();
        }
    }

    public void startDownloadFragment(SingleImage image){

        zoomImage = image;
        downloadFragment = new DownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", image.getImageUrl());
        bundle.putString("title", image.getTitle());

        downloadFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, downloadFragment)
                .commit();
    }

    public void saveImageToGallery(){

        Glide.with(this)
                .load(zoomImage.getImageUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
                        print("Came here");
                        bitmapImage = resource;
                        saveImage(resource);

                    }
                });
    }

    private void saveImage(Bitmap image) {
        //String savedImagePath = null;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }else{

            SaveToGallery saver = new SaveToGallery(this,image);
            saver.execute();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    saveImage(bitmapImage);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_denied)
                            , Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    String getSearchHistory(){
        String search_history;
        SharedPreferences mPreferences = this.getSharedPreferences("search_history", Context.MODE_PRIVATE);
        search_history = mPreferences.getString(SEARCH_PREFERENCES, "flickr");
        return search_history;

    }

    void setSearchHistory(String newSearch){
        String search_history;
        SharedPreferences mPreferences = this.getSharedPreferences("search_history", Context.MODE_PRIVATE);
        search_history = mPreferences.getString(SEARCH_PREFERENCES, "flickr");

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SEARCH_PREFERENCES,search_history+","+newSearch);
        editor.commit();

    }

    void clearSearch(){
        String search_history = "flickr";
        SharedPreferences mPreferences = this.getSharedPreferences("search_history", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SEARCH_PREFERENCES,search_history);
        editor.commit();
    }

    public void searchFromHistory(String search){

        setSearchHistory(search);
        spinner.setVisibility(View.VISIBLE);
        makeRestCall(search);

    }

    public void clearFromHistory(){
        clearSearch();
    }

    public void startProgressBar(){
        spinner.setVisibility(View.VISIBLE);
    }


}
