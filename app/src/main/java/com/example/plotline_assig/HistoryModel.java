package com.example.plotline_assig;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "imagesDb")
public class HistoryModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "Original")
    String Original;

    @ColumnInfo(name = "Converted")
    String Converted;

    public HistoryModel(String original, String converted) {
        Original = original;
        Converted = converted;
    }

    public HistoryModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal() {
        return Original;
    }

    public void setOriginal(String original) {
        Original = original;
    }

    public String getConverted() {
        return Converted;
    }

    public void setConverted(String converted) {
        Converted = converted;
    }
}
