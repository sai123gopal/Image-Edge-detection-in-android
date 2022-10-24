package com.example.plotline_assig;
import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {HistoryModel.class},version = 1,exportSchema = false)
public abstract class HistoryDb extends RoomDatabase {

    public abstract HistoryDao getHistoryDao();
}
