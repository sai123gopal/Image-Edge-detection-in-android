package com.example.plotline_assig;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert()
    void addData(HistoryModel historyModel);

    @Query("SELECT * FROM imagesDb WHERE Original NOT NULL ORDER BY id DESC")
    LiveData<List<HistoryModel>> getData();

}
