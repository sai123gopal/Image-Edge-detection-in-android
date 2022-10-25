package com.example.plotline_assig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.color.DynamicColors;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    ArrayList<HistoryModel> list;
    HistoryDbClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resulty);

        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        ResultsAdapter adapter = new ResultsAdapter(list,this);

        recyclerView.setAdapter(adapter);
        client = new HistoryDbClient(this);

        client.getData().observe(this, historyModels -> {
            list.addAll(historyModels);
            adapter.notifyDataSetChanged();
        });


    }
}