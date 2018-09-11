package com.example.shereen.picturesearch.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.shereen.picturesearch.MainActivity;

import static com.example.shereen.picturesearch.MainActivity.SEARCH_PREFERENCES;

public class SaveToHistory extends AsyncTask<Void,Void,Void> {

    Context context;
    String search;
    ProgressBar progressBar;

    public SaveToHistory(Context context, String search, ProgressBar progressBar){
        this.context = context;
        this.search = search;
        this.progressBar = progressBar;

    }

    @Override
    protected void onPreExecute() {

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {

        addToSearch();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }

    void addToSearch(){
        String[] searcheArray = getSearchHistory().split(",");

        boolean found = false;
        for(String content:searcheArray){
            if(content.equals(search)){
               found = true;
            }
        }
        if(!found){
            setSearchHistory(search);
        }

    }

    String getSearchHistory(){
        String search_history;
        SharedPreferences mPreferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE);
        search_history = mPreferences.getString(SEARCH_PREFERENCES, "flickr");
        return search_history;

    }

    void setSearchHistory(String newSearch){
        String search_history;
        SharedPreferences mPreferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE);
        search_history = mPreferences.getString(SEARCH_PREFERENCES, "flickr");

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SEARCH_PREFERENCES,search_history+","+newSearch);
        editor.commit();

    }
}
