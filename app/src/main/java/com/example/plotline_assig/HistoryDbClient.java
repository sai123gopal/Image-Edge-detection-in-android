package com.example.plotline_assig;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

public class HistoryDbClient {

    private HistoryDb historyDb;
    public Context context;

    public HistoryDbClient(Context context) {
        this.context = context;
        this.historyDb = Room.databaseBuilder(context,HistoryDb.class,"imagesDb").build();
    }

    public void insert(HistoryModel model){
        new AddData().execute(model);
    }
    public LiveData<List<HistoryModel>> getData(){
        return historyDb.getHistoryDao().getData();
    }

    @SuppressLint("StaticFieldLeak")
    public class AddData extends AsyncTask<HistoryModel,Void,Exception> {

        @Override
        protected Exception doInBackground(HistoryModel... historyModels) {
            historyDb.getHistoryDao().addData(historyModels[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
        }
    }
}
